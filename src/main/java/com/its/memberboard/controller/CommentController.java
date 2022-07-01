package com.its.memberboard.controller;

import com.its.memberboard.dto.CommentDTO;
import com.its.memberboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    public String save(@ModelAttribute CommentDTO commentDTO) {
        Long boardId = commentDTO.getBoardId();
        commentService.save(commentDTO);
        return "redirect:/board/" + boardId;
    }

}
