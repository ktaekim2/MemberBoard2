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

import javax.servlet.http.HttpSession;
import java.io.IOException;

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

    // 파일첨부 부분 주석처리 필요
    @Test
    @DisplayName("회원가입 테스트")
    @Transactional
    @Rollback(value = true)
    public void memberSaveTest() throws IOException {
        Long saveId = memberService.save(newMember(1));
        MemberDTO memberDTO = memberService.findById(saveId);
        assertThat(newMember(1).equals(memberDTO));
    }

    // 파일첨부 부분 주석처리 필요
    @Test
    @DisplayName("로그인 테스트")
    @Transactional
    @Rollback(value = true)
    public void loginTest() throws IOException {
        memberService.save(newMember(1));
        String loginEmail = newMember(1).getMemberEmail();
        String loginPassword = newMember(1).getMemberPassword();

        MemberDTO loginMemberDTO = new MemberDTO();
        loginMemberDTO.setMemberEmail(loginEmail);
        loginMemberDTO.setMemberPassword(loginPassword);

        MemberDTO loginResult = memberService.login(loginMemberDTO);
        assertThat(loginResult).isNotNull();
    }
}
