package com.spring.myproject.service;

import com.spring.myproject.constant.Role;
import com.spring.myproject.dto.MemberDTO;
import com.spring.myproject.entity.Member;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface MemberService {

    Member saveMember(@Valid MemberDTO member);

    //1. 방법 : dtoToEntity : MemberDTO => 암호화 => Entity
    default Member dtoToEntity(MemberDTO memberDTO, PasswordEncoder passwordEncoder){
        Member member = new Member();

        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setAddress(memberDTO.getAddress());

        //비밀번호 암호화 작업!
        String password = passwordEncoder.encode(memberDTO.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);

        return member;
    }
}
