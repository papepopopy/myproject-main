package com.spring.myproject.service;

import com.spring.myproject.constant.Role;
import com.spring.myproject.dto.MemberDTO;
import com.spring.myproject.entity.Member;
import com.spring.myproject.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = {"classpath:application-test.properties"})
@Log4j2
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("Member Entity 생성")
    // 회원 정보 DTO, Entity생성하기
    public void createMember(){
        // 클라이언트로부터 전달받은
        // 더미 data MemberDTO 생성
        IntStream.rangeClosed(1, 10).forEach(i -> {

            Member member = Member.builder()
                    .email("test"+i+"@email.com")
                    .name("홍길동"+i)
                    .address("부산시 진구")
                    .password(passwordEncoder.encode("1234"))
                    .build();

            member.addRole(Role.USER);

            if(i>= 5) {
                member.addRole(Role.ADMIN);
            }

            Member saveMember = memberRepository.save(member);
        });

        //----------------------------------//
        // dto -> 암호화 작업 -> entity
        //----------------------------------//

        // 1. dto->entity : Member클래스 createMember메서드 활용
        //return Member.createMember(memberDTO, passwordEncoder);

        // 2. dto->entity :인터페이스에서 정의한 메서드 활용
        //return memberService.dtoToEntity(memberDTO, passwordEncoder);
//        return memberDTO;
//        return null;
    }

    @Test
    @DisplayName("회원 조회 Test")
    public void insertMemberRead() {
        IntStream.rangeClosed(8, 9).forEach(i -> {
            String username = "test"+i+"@email.com";
            Member member = memberRepository.findByEmail(username);
            log.info(member.getEmail());

            member.getRoleSet().forEach(role -> log.info("=> member role : "+role.name()));
        });
    }

    public MemberDTO createMember1() {
        MemberDTO memberDTO = MemberDTO.builder()
                .email("test1000@email.com")
                .name("홍길동")
                .address("부산시 진구")
                .password("1234")
                .build();

        return memberDTO;
    }


    @Test
    @DisplayName("회원 가입 서비스 테스트")
    public void saveDuplicateMemberTest(){

        // 회원1
        MemberDTO memberDTO = createMember();
        // 1. dto -> entity: Member Entity createMember() 메서드 활용
//        Member member = Member.createMember(memberDTO, passwordEncoder);
        // 2. dto -> entity: MeberService 인터페이스 활용
        // save전 entity
        Member member = memberService.dtoToEntity(memberDTO, passwordEncoder );
        // save후 entity
        Member savedMember = memberService.saveMember(memberDTO);


        // 회원 등록 테스트 결과 체크: assertEquals(기대값, 실제값)
        assertEquals(member.getEmail(),     savedMember.getEmail(),  "이메일이 일치하지 않습니다.");
        assertEquals(member.getAddress(),   savedMember.getAddress(), "주소가 일치하지 않습니다.");
        assertEquals(member.getPassword(),  savedMember.getPassword(), "비밀번호가 일치하지 않습니다.");
//        assertEquals(member.getRole(),      savedMember.getRole(), "역할이 일치하지 않습니다.");
        assertEquals(member.getName(),      savedMember.getName());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveMemberTest(){

        // 회원1
//        MemberDTO memberDTO1 = createMember();
//        memberService.saveMember(memberDTO1);

        // 회원2
        MemberDTO memberDTO2 = createMember();

        // assertThrows(예외 발생 타입, 실제 예외발생) 메서드: 예외 처리 테스트 메서드
        // 중복된 이메일 회원등록시 예외발생시 객체 생성
        Throwable e = assertThrows(IllegalStateException.class, () ->{
            // 예외 발생 타입, 실제 예외발생
            memberService.saveMember(memberDTO2);
        });

        // 예외 발생 메시지 동일 여부 확인
        assertEquals("이미 가입된 회원 입니다.", e.getMessage());
        log.info("=> e.getMessage():" + e.getMessage());
    }
}