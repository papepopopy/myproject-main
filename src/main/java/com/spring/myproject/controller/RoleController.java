package com.spring.myproject.controller;

import com.spring.myproject.entity.Member;
import com.spring.myproject.repository.MemberRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("Role")
public class RoleController {
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    @GetMapping("/user")
//    public @ResponseBody String roleUserOrAdmin() {
//        return "Role: USER or ADMIN";
//    }
    private final MemberRepository memberRepository;
    @PreAuthorize(value = "ROLE_ADMIN")
    @GetMapping("/secured")
    public @ResponseBody String roleSe() {
        return "@Secured(value='USER')";
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public @ResponseBody String roleUser() {
        return "@Secured(value='USER')";
    }

    @PostAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/admin")
    public @ResponseBody String roleAdmin() {
        return "@Secured(value='ADMIN' 또는 'USER' 이고 isAuthenticated)";
    }

    @PostAuthorize("isAuthenticated() and ((returnObject.name == principal.name) or hasRole('ADMIN'))")
    @GetMapping("/selectOne/{username}")
    public @ResponseBody Member roleSelectOneUser(@PathVariable("username") String username) {
        return memberRepository.findByEmail(username);
    }
}