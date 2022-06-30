package com.its.memberboard.service;

import com.its.memberboard.dto.MemberDTO;
import com.its.memberboard.entity.MemberEntity;
import com.its.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long save(MemberDTO memberDTO) throws IOException {
        MultipartFile memberProfile = memberDTO.getMemberProfile();
        String memberProfileName = memberProfile.getOriginalFilename();
        memberProfileName = System.currentTimeMillis() + "_" + memberProfileName;
        String savePath = "D:\\springboot_img\\" + memberProfileName;

        if (!memberProfile.isEmpty())
            memberProfile.transferTo(new File(savePath));
        memberDTO.setMemberProfileName(memberProfileName);

        Long saveId = memberRepository.save(MemberEntity.toEntity(memberDTO)).getId();
        return saveId;
    }

    public Long testSave(MemberDTO memberDTO) {
        Long saveId = memberRepository.save(MemberEntity.toEntity(memberDTO)).getId();
        return saveId;
    }

    public MemberDTO login(MemberDTO memberDTO) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                return MemberDTO.toMemberDTO(memberEntity);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity m :
                memberEntityList) {
            memberDTOList.add(MemberDTO.toMemberDTO(m));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long saveId) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(saveId);
        if (optionalMemberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toEntity(memberDTO));
    }

    public boolean duplicateCheck(String memberEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberEmail);
        if (optionalMemberEntity.isPresent()) {
            return false;
        } else {
            return true;
        }
    }
}
