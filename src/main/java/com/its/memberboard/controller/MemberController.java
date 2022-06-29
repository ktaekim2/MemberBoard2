package com.its.memberboard.controller;

import com.its.memberboard.dto.MemberDTO;
import com.its.memberboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.IOException;

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
            return "redirect:/board/paging";
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
}
