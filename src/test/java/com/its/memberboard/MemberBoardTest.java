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

@SpringBootTest
public class MemberBoardTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    public MemberDTO newMember() {
        MemberDTO memberDTO = new MemberDTO("admin", "1234");
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
}
