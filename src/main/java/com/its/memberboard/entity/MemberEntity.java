package com.its.memberboard.entity;

import com.its.memberboard.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "member_table")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 30, nullable = false)
    private String memberEmail;

    @Column(length = 30, nullable = false)
    private String memberPassword;

    @Column(length = 30)
    private String memberName;

    @Column(length = 20)
    private String memberMobile;

    @Column
    private String memberProfileName;

    // on delete cascade(부모데이터를 지우면 자식 데이터도 같이 지워짐)
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardEntity> boardEntityList = new ArrayList<>();

    public static MemberEntity toEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberMobile(memberDTO.getMemberMobile());
        memberEntity.setMemberProfileName(memberDTO.getMemberProfileName());
        return memberEntity;
    }
}
