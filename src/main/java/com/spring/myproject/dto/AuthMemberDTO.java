package com.spring.myproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class AuthMemberDTO extends User {
    private String name;
    private String email;
    //private String password;
    private String address;

    public AuthMemberDTO(
            String username,
            String address,
            String password,
            Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);

        this.email = username;
        this.address = address;
    }
}
