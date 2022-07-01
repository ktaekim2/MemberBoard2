package com.its.memberboard.controller;

import com.its.memberboard.common.PagingConst;
import com.its.memberboard.dto.BoardDTO;
import com.its.memberboard.dto.CommentDTO;
import com.its.memberboard.service.BoardService;
import com.its.memberboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
        System.out.println("BoardController.paging");
        Page<BoardDTO> boardList = boardService.paging(pageable);
        model.addAttribute("boardList", boardList);
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.BLOCK_LIMIT))) - 1) * PagingConst.BLOCK_LIMIT + 1;
        int endPage = ((startPage + PagingConst.BLOCK_LIMIT - 1) < boardList.getTotalPages()) ? startPage + PagingConst.BLOCK_LIMIT - 1 : boardList.getTotalPages();
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "/boardPages/paging";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList", commentDTOList);
        return "/boardPages/detail";
    }

    @GetMapping("/save-form")
    public String saveForm() {
        return "/boardPages/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        boardService.save(boardDTO);
        return "redirect:/board";
    }

    @GetMapping("/update-form/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "/boardPages/update";
    }

    @PutMapping("/update/{id}")
    public ResponseEntity update(@RequestBody BoardDTO boardDTO) {
        System.out.println("boardDTO = " + boardDTO);
        boardService.update(boardDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        boardService.deleteById(id);
        return "redirect:/board";
    }

    @GetMapping("/search")
    public String search(@RequestParam("searchType") String searchType, @RequestParam("q") String q, Model model, @PageableDefault(page = 1) Pageable pageable) {
        if (searchType == "boardTitle") {
            Page<BoardDTO> boardList = boardService.searchTitle(q, pageable);
            model.addAttribute("boardList", boardList);
            int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.BLOCK_LIMIT))) - 1) * PagingConst.BLOCK_LIMIT + 1;
            int endPage = ((startPage + PagingConst.BLOCK_LIMIT - 1) < boardList.getTotalPages()) ? startPage + PagingConst.BLOCK_LIMIT - 1 : boardList.getTotalPages();
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "boardPages/paging";
        }
        else {
            Page<BoardDTO> boardList = boardService.searchWriter(q, pageable);
            model.addAttribute("boardList", boardList);
            int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.BLOCK_LIMIT))) - 1) * PagingConst.BLOCK_LIMIT + 1;
            int endPage = ((startPage + PagingConst.BLOCK_LIMIT - 1) < boardList.getTotalPages()) ? startPage + PagingConst.BLOCK_LIMIT - 1 : boardList.getTotalPages();
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "boardPages/paging";
        }
    }
}
