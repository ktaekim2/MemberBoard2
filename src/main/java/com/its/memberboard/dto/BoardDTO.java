package com.its.memberboard.dto;

import com.its.memberboard.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long id;
    private String boardTitle;
    private String boardWriter;
    private String boardContents;
    private int boardHits;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private MultipartFile boardFile;
    private String boardFileName;

    public BoardDTO(String boardTitle, String boardWriter, String boardContents) {
        this.boardTitle = boardTitle;
        this.boardWriter = boardWriter;
        this.boardContents = boardContents;
    }

    public BoardDTO(Long id, String boardTitle, String boardWriter, String boardContents) {
        this.id = id;
        this.boardTitle = boardTitle;
        this.boardWriter = boardWriter;
        this.boardContents = boardContents;
    }

    public BoardDTO(Long id, String boardTitle, String boardWriter, int boardHits) {
        this.id = id;
        this.boardTitle = boardTitle;
        this.boardWriter = boardWriter;
        this.boardHits = boardHits;
    }

    public BoardDTO(Long id, String boardTitle, String boardWriter, int boardHits, LocalDateTime createdTime, LocalDateTime updatedTime) {
        this.id = id;
        this.boardTitle = boardTitle;
        this.boardWriter = boardWriter;
        this.boardHits = boardHits;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public static BoardDTO toDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setUpdatedTime(boardEntity.getUpdatedTime());
        boardDTO.setBoardFileName(boardEntity.getBoardFileName());
        return boardDTO;
    }
}
