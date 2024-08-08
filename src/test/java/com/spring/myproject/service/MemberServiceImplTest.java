package com.spring.myproject.service;

import com.spring.myproject.dto.MemberDTO;
import com.spring.myproject.entity.Member;
import com.spring.myproject.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = {"classpath:application-test.properties"})
class MemberServiceImplTest {
    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    // 회원 정보 DTO, Entity생성하기
    public Member createMember() {
        MemberDTO memberDTO = MemberDTO.builder()
                .email("test@email.com")
                .name("홍길동")
                .address("부산 광역시")
                .password("1234")
                .build();
//        return Member.createMember(memberDTO, passwordEncoder);
        return memberService.dtoToEntity(memberDTO, passwordEncoder);
    }

    @Test
    @DisplayName("회원등록 서비스 테스트")
    public void saveMemberTest(){
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        //앞에 값에 맞춰 바뀐다.
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());

    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest(){
        //회원 1
        Member member1 = createMember();
        memberService.saveMember(member1);
        //회원 2
        Member member2 = createMember();

        Throwable e = assertThrows(
                IllegalAccessException.class, () -> {
            memberService.saveMember(member2);
        });

        //예외 발생 메시지 안내 메세지
        assertEquals("이미 가입되어있는 회원입니다.", e.getMessage());

    }
}