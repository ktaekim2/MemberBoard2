package com.its.memberboard.entity;

import com.its.memberboard.dto.BoardDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "board_table")
public class BoardEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(length = 30, nullable = true)
    private String boardTitle;

    @Column(length = 30, nullable = true)
    private String boardWriter;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    @Column
    private String boardFileName;

    //회원(1)-게시글(n) 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    // FetchType: LAZY(필요할 때 호출한 시점에 가져옴), EAGER(게시글 조회할 때 댓글 목록을 쓰던 말던 같이 가져옴, 불필요한 정보를 가져옴)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    public static BoardEntity toEntity(BoardDTO boardDTO, MemberEntity memberEntity) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardWriter(memberEntity.getMemberEmail()); //회원 이메일을 작성자로 한다면
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0); //초깃값 0
        boardEntity.setBoardFileName(boardDTO.getBoardFileName());
        boardEntity.setMemberEntity(memberEntity); //entity전체가 아닌, member_id값만 테이블에 들어감
        return boardEntity;
    }
}
