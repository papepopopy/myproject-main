package com.spring.myproject.controller;

import com.spring.myproject.dto.MemberDTO;
import com.spring.myproject.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
//@TestPropertySource
class MemberControllerTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public MemberDTO createMember(String email, String password) {

        MemberDTO memberDTO = MemberDTO.builder()
            .email(email).password(password).name("홍길동").address("test")
            .build();

        memberService.saveMember(memberDTO);
        return memberDTO;
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email = "test@email.com";
        String password = "1234";

        //회원 생성
        this.createMember(email, password);

        //로그인 테스트
        mockMvc.perform(formLogin()
                .userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email)
            .password(password))
            .andDo(print()) // 출력
        .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        String email = "test2000@email.com";
        String password = "1234";

        //회원 생성
        this.createMember(email, password);

        //로그인 테스트
        mockMvc.perform(formLogin()
                .userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email)
                .password("12345"))
            .andDo(print()) // 출력
            .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }
}