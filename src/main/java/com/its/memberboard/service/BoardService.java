package com.its.memberboard.service;

import com.its.memberboard.common.PagingConst;
import com.its.memberboard.dto.BoardDTO;
import com.its.memberboard.entity.BoardEntity;
import com.its.memberboard.entity.MemberEntity;
import com.its.memberboard.repository.BoardRepository;
import com.its.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public List<BoardDTO> findAll() {
        System.out.println("BoardService.findAll");
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList
        ) {
            boardDTOList.add(BoardDTO.toDTO(boardEntity));
        }
        System.out.println("boardDTOList = " + boardDTOList);
        return boardDTOList;
    }

    public void save(BoardDTO boardDTO) throws IOException {
        MultipartFile boardFile = boardDTO.getBoardFile();
        String originalFilename = boardFile.getOriginalFilename();
        originalFilename = System.currentTimeMillis() + "_" + originalFilename;
        String savePath = "D:\\springboot_img\\" + originalFilename;
        if (!boardFile.isEmpty()) {
            boardFile.transferTo(new File(savePath));
        }
        boardDTO.setBoardFileName(originalFilename);

        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(boardDTO.getBoardWriter());
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            boardRepository.save(BoardEntity.toEntity(boardDTO, memberEntity));
        }
    }

    public Long saveTest(BoardDTO boardDTO) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(boardDTO.getBoardWriter());
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            Long saveId = boardRepository.save(BoardEntity.toEntity(boardDTO, memberEntity)).getId();
            return saveId;
        }
        return null;
    }

    @Transactional
    public BoardDTO findById(Long id) {
        boardRepository.boardHits(id);
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            return BoardDTO.toDTO(boardEntity);
        }
        return null;
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber(); // 요청 페이지값 가져옴
        // 요청한 페이지가 1이면 페이지값을 0으로 하고 1이 아니면 요청 페이지에서 1을 뺀다.
//        page = page - 1;
        page = (page == 1) ? 0 : (page - 1); // 삼항연산자
        // 매서드 오버로딩? 오버라이딩?
        // PageRequest: 매서드 오버로딩
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));
        // "id" 부분은 카멜케이스로 써야함.
        // Page<BoardEntity> => Page<BoardDTO>
        // map: page객체가 제공하는 변환 메서드, 자동으로 옮겨담아줌
        Page<BoardDTO> boardList = boardEntities.map(
                // BoardEntity 객체 -> BoardDTO 객체 변환
                // board: BoardEntity 객체
                // new BoardDTO() 생성자 호출
                board -> new BoardDTO(board.getId(), // 화살표 함수는 변수(board)만 써줘도 인식 됨
                        board.getBoardTitle(),
                        board.getBoardWriter(),
                        board.getBoardHits(),
                        board.getBoardCreatedDate()
                ));
        return boardList;
    }

    public void update(BoardDTO boardDTO) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(boardDTO.getBoardWriter());
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            boardRepository.save(BoardEntity.toUpdateEntity(boardDTO, memberEntity));
        }
    }

    public void deleteById(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    public Page<BoardDTO> search(String searchType, String q, Pageable pageable) {
        int page = pageable.getPageNumber();
        page = (page == 1) ? 0 : (page - 1); // 삼항연산자
        Page<BoardEntity> boardEntities = boardRepository.search(q, PageRequest.of(page, PagingConst.PAGE_LIMIT, Sort.by(Sort.Direction.DESC, "id")));

        Page<BoardDTO> boardList = boardEntities.map(
                board -> new BoardDTO(board.getId(),
                        board.getBoardTitle(),
                        board.getBoardWriter(),
                        board.getBoardHits(),
                        board.getBoardCreatedDate()
                ));
        System.out.println("boardList = " + boardList);
        return boardList;
    }
}
