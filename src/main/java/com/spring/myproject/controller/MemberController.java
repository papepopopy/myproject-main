package com.spring.myproject.controller;

import com.spring.myproject.dto.MemberDTO;
import com.spring.myproject.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    //회원 등록 get, post
    @GetMapping(value = "/new")
    public String memberRegisterForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());

        return "members/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberRegister(@Valid @ModelAttribute
                                MemberDTO memberDTO,
                                BindingResult bindingResult,
                                Model model) {
        log.info("=> memberDTO" + memberDTO);
        if (bindingResult.hasErrors()) {
            log.info("=> 에러" + bindingResult.toString());
            //1개 이상의 에러 시 돌아가기
            return "members/memberForm";
        }
        try {
            // dto => entity => email중복 체크 => save
            memberService.saveMember(memberDTO);
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "members/memberForm";
        }
        return "redirect:/";
    }

    //로그인 처리
    @GetMapping(value = "/login")
    public String loginMember(String error, String logout) {
        log.info("=> login");

        return "members/loginForm";
    }
    //로그인 실패시
    @GetMapping("/login/error")
    public String loginError(Model model) {
        log.info("=> login error");

        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해줘세요");
        return "members/loginForm";
    }

    //메서드 단위 권한 설정 여부테스트
    //@Secured(value = "ROLE_ADMIN")
//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/role_test1")
//    @ResponseBody
//    public String info() {
//        return "@Secured(value = 'ROLE_ADMIN')";
//    }
}

