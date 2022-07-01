package com.its.memberboard.controller;

import com.its.memberboard.dto.MemberDTO;
import com.its.memberboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save-form")
    public String saveForm() {
        return "/memberPages/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) throws IOException {
        memberService.save(memberDTO);
        return "/memberPages/login";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "/memberPages/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        System.out.println("MemberController.login");
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            System.out.println("로그인 성공");
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            session.setAttribute("loginId", loginResult.getId());
            return "redirect:/board";
        } else {
            System.out.println("로그인 실패");
            return "/memberPages/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        System.out.println("MemberController.logout");
        return "index";
    }

    @GetMapping("/admin")
    public String admin() {
        return "/memberPages/admin";
    }

    @GetMapping("/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "memberPages/list";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/main")
    public String main(HttpSession session, Model model) {
        MemberDTO memberDTO = memberService.findById((Long) session.getAttribute("loginId"));
        model.addAttribute("member", memberDTO);
        return "/memberPages/main";
    }

    @GetMapping("/update-form")
    public String updateForm(Model model, HttpSession session) {
        MemberDTO memberDTO = memberService.findById((Long) session.getAttribute("loginId"));
        model.addAttribute("member", memberDTO);
        return "/memberPages/update";
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody MemberDTO memberDTO) throws IOException {
        System.out.println("MemberController.update");
        System.out.println("memberDTO = " + memberDTO);
        memberService.update(memberDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/duplicate-check")
    public ResponseEntity duplicateCheck(@RequestParam String memberEmail) {
        boolean checkResult = memberService.duplicateCheck(memberEmail);
        if (checkResult) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
