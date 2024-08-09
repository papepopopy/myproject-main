package com.spring.myproject.entity;


import com.spring.myproject.constant.Role;
import com.spring.myproject.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@Table(name="member")
@Getter@Setter
@ToString(exclude = "password") //민감정보 보호
public class Member {
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)  // 회원은 이메일에 동일한 값이 허용할 수 없도록 설정
    private String email;
    private String password;
    private String address;
    //2-3. db에 있는 회원정보를 User 객체를 통해 전달받은 AuthMemberDTO 객체 생성(CustomUserDetailsService 파일)
//    @Enumerated(EnumType.STRING)
//    private Role role;
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default //to string 쓰지 않겠다.
    private Set<Role> roles = new HashSet<>();
    //HashSet => 중복된 값은 저장되어지지 않는다

    public void changePassword(String mpw) {
        this.password = mpw;
    }
    public void changeEmail(String email) {
        this.email = email;
    }
    public void addRole(Role role) {
        this.roles.add(role);
    }
    public void clearRole() {
        this.roles.clear();
    }


//    public static Member createMember(MemberDTO memberDTO,
//                                      PasswordEncoder passwordEncoder){
//        Member member = new Member();
//
//        member.setName(memberDTO.getName());
//        member.setEmail(memberDTO.getEmail());
//        member.setAddress(memberDTO.getAddress());
//
//        // 비밀번호 -> 암호화 작업
//        String password = passwordEncoder.encode(memberDTO.getPassword());
//        member.setPassword(password);
////        member.setRole(Role.USER);
////        member.setRole(Role.ADMIN);
//        member.addRole(Role.USER);
//
//        return member;
//    }



}