package com.its.memberboard;

import com.its.memberboard.common.PagingConst;
import com.its.memberboard.dto.BoardDTO;
import com.its.memberboard.dto.MemberDTO;
import com.its.memberboard.entity.BoardEntity;
import com.its.memberboard.entity.MemberEntity;
import com.its.memberboard.repository.BoardRepository;
import com.its.memberboard.repository.MemberRepository;
import com.its.memberboard.service.BoardService;
import com.its.memberboard.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberBoardTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;

    public MemberDTO newMember(int i) {
        MemberDTO memberDTO = new MemberDTO("test-email" + i, "test-pw" + i, "test-name" + i, "test-phone" + i);
        return memberDTO;
    }

    @Test
    @DisplayName("admin 저장")
    @Transactional
    @Rollback(value = false)
    public void adminSave() {
        MemberDTO memberDTO = new MemberDTO("admin", "1234");
        memberRepository.save(MemberEntity.toEntity(memberDTO));
    }

    @Test
    @DisplayName("회원 저장")
    public void memberSave() {
        IntStream.rangeClosed(1, 20).forEach(i -> {
            try {
                memberService.save(newMember(i));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    @DisplayName("회원가입 테스트")
    @Transactional
    @Rollback(value = true)
    public void memberSaveTest() {
        Long saveId = memberService.testSave(newMember(1));
        MemberDTO memberDTO = memberService.findById(saveId);
        assertThat(newMember(1).equals(memberDTO));
    }

    @Test
    @DisplayName("로그인 테스트")
    @Transactional
    @Rollback(value = true)
    public void loginTest() {
        memberService.testSave(newMember(1));
        String loginEmail = newMember(1).getMemberEmail();
        String loginPassword = newMember(1).getMemberPassword();

        MemberDTO loginMemberDTO = new MemberDTO();
        loginMemberDTO.setMemberEmail(loginEmail);
        loginMemberDTO.setMemberPassword(loginPassword);

        MemberDTO loginResult = memberService.login(loginMemberDTO);
        assertThat(loginResult).isNotNull();
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    @Transactional
    @Rollback(value = true)
    public void deleteTest() throws IOException {
        Long saveId = memberService.testSave(newMember(999));
        memberService.deleteById(saveId);
        assertThat(memberService.findById(saveId)).isNull();
    }

    @Test
    @DisplayName("회원 정보수정 테스트")
    @Transactional
    @Rollback(value = true)
    public void memberUpdateTest() {
        Long saveId = memberService.testSave(newMember(888));
        MemberDTO updateMemberDTO = memberService.findById(saveId);
        System.out.println("updateMemberDTO = " + updateMemberDTO);
        updateMemberDTO.setMemberName("암거나");
        updateMemberDTO.setMemberMobile("흐음");
        memberService.update(updateMemberDTO);
        MemberDTO updatedMemberDTO = memberService.findById(saveId);
        System.out.println("updatedMemberDTO = " + updatedMemberDTO);
    }

    @Test
    @Transactional
    @DisplayName("페이징 테스트")
    public void pagingTest() {
        int page = 2;
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));
        // Page 객체가 제공해주는 메서드 확인
        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫페이지인지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막페이지인지 여부

        Page<BoardDTO> boardList = boardEntities.map(
                board -> new BoardDTO(board.getId(),
                        board.getBoardTitle(),
                        board.getBoardWriter(),
                        board.getBoardHits(),
                        board.getBoardCreatedDate()
                ));

        System.out.println("boardList.getContent() = " + boardList.getContent()); // 요청페이지에 들어있는 데이터
        System.out.println("boardList.getTotalElements() = " + boardList.getTotalElements()); // 전체 글갯수
        System.out.println("boardList.getNumber() = " + boardList.getNumber()); // 요청페이지(jpa 기준)
        System.out.println("boardList.getTotalPages() = " + boardList.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardList.getSize() = " + boardList.getSize()); // 한페이지에 보여지는 글갯수
        System.out.println("boardList.hasPrevious() = " + boardList.hasPrevious()); // 이전페이지 존재 여부
        System.out.println("boardList.isFirst() = " + boardList.isFirst()); // 첫페이지인지 여부
        System.out.println("boardList.isLast() = " + boardList.isLast()); // 마지막페이지인지 여부

    }

    public BoardDTO newBoard(int i) {
        BoardDTO boardDTO = new BoardDTO("test-title" + i, "test-email" + i, "test-contents" + i);
        return boardDTO;
    }

    @Test
    @DisplayName("게시글 저장 테스트")
    @Transactional
    @Rollback(value = true)
    public void boardSaveTest() {
        memberService.testSave(newMember(777));
        Long saveId = boardService.saveTest(newBoard(777));
        BoardDTO boardDTO = boardService.findById(saveId);
        assertThat(newMember(777).equals(boardDTO));
    }

    @Test
    @DisplayName("회원-게시글 연관관계 저장 테스트")
    @Transactional
    @Rollback(value = true)
    public void memberBoardSaveTest() {
        memberService.testSave(newMember(123));
        Long boardId = boardService.saveTest(newBoard(123));
        BoardDTO boardDTO = boardService.findById(boardId);
        assertThat(newMember(123).getMemberEmail().equals(boardDTO.getBoardWriter()));
    }

    @Test
    @DisplayName("회원-게시글 연관관계 조회 테스트")
    @Transactional
    @Rollback(value = true)
    public void memberBoardFindByIdTest() {
        memberService.testSave(newMember(111));
        Long boardId = boardService.saveTest(newBoard(111));
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(boardId);
        if(optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            System.out.println("boardEntity.getMemberEntity().getMemberName() = " + boardEntity.getMemberEntity().getMemberName());
        }
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    @Transactional
    @Rollback(value = true)
    public void memberBoardUpdateTest() {
        memberService.testSave(newMember(333));
        Long boardId = boardService.saveTest(newBoard(333));
        BoardDTO boardDTO = boardService.findById(boardId);
        System.out.println("boardDTO = " + boardDTO);
        boardDTO.setBoardTitle("수정제목");
        boardDTO.setBoardContents("수정내용");
        boardService.update(boardDTO);
        BoardDTO updatedBoardDTO = boardService.findById(boardId);
        System.out.println("updatedBoardDTO = " + updatedBoardDTO);
        assertThat(!boardDTO.equals(updatedBoardDTO));
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    @Transactional
    @Rollback(value = true)
    public void memberBoardDeleteTest() {
        memberService.testSave(newMember(444));
        Long boardId = boardService.saveTest(newBoard(444));
        boardService.deleteById(boardId);
        assertThat(boardService.findById(boardId) == null);
    }
}
