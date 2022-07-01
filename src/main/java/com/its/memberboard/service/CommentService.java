package com.its.memberboard.service;

import com.its.memberboard.dto.CommentDTO;
import com.its.memberboard.entity.BoardEntity;
import com.its.memberboard.entity.CommentEntity;
import com.its.memberboard.entity.MemberEntity;
import com.its.memberboard.repository.BoardRepository;
import com.its.memberboard.repository.CommentRepository;
import com.its.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public List<CommentDTO> findAll(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            List<CommentEntity> commentEntityList = boardEntity.getCommentEntityList();
            for (CommentEntity commentEntity : commentEntityList) {
                commentDTOList.add(CommentDTO.toDTO(commentEntity));
            }
        }
        return commentDTOList;
    }

    public void save(CommentDTO commentDTO) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(commentDTO.getCommentWriter());
        if(optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            BoardEntity findBoardEntity = boardRepository.findById(commentDTO.getBoardId()).get();
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, findBoardEntity, memberEntity);
            commentRepository.save(commentEntity);
        }
    }
}
