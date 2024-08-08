package com.spring.myproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    @NotBlank(message = "필수 입력값입니다.")
    private String name;

    @NotEmpty(message = "필수 입력값입니다.")
    private String email;

    @NotEmpty(message = "필수 입력값입니다.")
    @Length(min=8, max=16, message="8자 이상 16자 이하로 입력")
    private String password;

    @NotEmpty(message = "필수 입력값입니다.")
    private String address;
}
