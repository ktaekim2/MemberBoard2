package com.its.memberboard.service;

import com.its.memberboard.dto.MemberDTO;
import com.its.memberboard.entity.MemberEntity;
import com.its.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO) throws IOException {
        MultipartFile memberProfile = memberDTO.getMemberProfile();
        String memberProfileName = memberProfile.getOriginalFilename();
        memberProfileName = System.currentTimeMillis() + "_" + memberProfileName;
        String savePath = "D:\\springboot_img\\" + memberProfileName;

        if (!memberProfile.isEmpty())
            memberProfile.transferTo(new File(savePath));
        memberDTO.setMemberProfileName(memberProfileName);
        memberRepository.save(MemberEntity.toEntity(memberDTO));
    }
}
