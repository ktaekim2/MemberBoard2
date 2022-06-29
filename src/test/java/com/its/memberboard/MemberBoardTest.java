package com.its.memberboard;

import com.its.memberboard.dto.MemberDTO;
import com.its.memberboard.entity.MemberEntity;
import com.its.memberboard.repository.MemberRepository;
import com.its.memberboard.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberBoardTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

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
    public void memberSaveTest() throws IOException {
        Long saveId = memberService.testSave(newMember(1));
        MemberDTO memberDTO = memberService.findById(saveId);
        assertThat(newMember(1).equals(memberDTO));
    }

    @Test
    @DisplayName("로그인 테스트")
    @Transactional
    @Rollback(value = true)
    public void loginTest() throws IOException {
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
    public void memberUpdateTest() throws IOException {
        Long saveId = memberService.testSave(newMember(888));
        MemberDTO updateMemberDTO = memberService.findById(saveId);
        System.out.println("updateMemberDTO = " + updateMemberDTO);
        updateMemberDTO.setMemberName("암거나");
        updateMemberDTO.setMemberMobile("흐음");
        memberService.update(updateMemberDTO);
        MemberDTO updatedMemberDTO = memberService.findById(saveId);
        System.out.println("updatedMemberDTO = " + updatedMemberDTO);
    }

}
