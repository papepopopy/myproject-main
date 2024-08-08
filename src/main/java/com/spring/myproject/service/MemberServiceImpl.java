package com.spring.myproject.service;

import com.spring.myproject.dto.MemberDTO;
import com.spring.myproject.entity.Member;
import com.spring.myproject.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 1. save
    public Member saveMember(@Valid MemberDTO memberDTO){
        //dto => entity
        //메서드
        Member member = Member.createMember(memberDTO, passwordEncoder);
        //인터페이스
        // Member member = dtoToEntity(memberDTO, passwordEncoder);

        // 회원 중복 체크(email기준) 메서드 호출
        validateDuplicateMember(member);

        // 중복된 이메일 없을 경우 저장(반영)
        return memberRepository.save(member);
    }

    // 1-1. 회원 중복 체크(email기준) 메서드
    // entity -> 이메일 유체크
    private void validateDuplicateMember(Member member){
        // Member Entity Email 기존에 Entity에 있는 유무 체크
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) throw new IllegalStateException("이미 가입된 회원 입니다.");
    }

}