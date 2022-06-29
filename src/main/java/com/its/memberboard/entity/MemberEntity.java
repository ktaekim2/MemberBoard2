package com.its.memberboard.entity;

import com.its.memberboard.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "member_table")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String memberEmail;

    @Column(length = 30, nullable = false)
    private String memberPassword;

    @Column(length = 30)
    private String memberName;

    @Column(length = 20)
    private String memberMobile;

    @Column
    private String memberProfileName;

    public static MemberEntity toEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberMobile(memberDTO.getMemberMobile());
        memberEntity.setMemberProfileName(memberDTO.getMemberProfileName());
        return memberEntity;
    }
}
