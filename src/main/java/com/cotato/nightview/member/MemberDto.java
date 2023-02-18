package com.cotato.nightview.member;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {      //requestdto -> 요청으로부터 정보를 받아 DB에 저장할 떄
    @NotBlank(message = "사용자 이름은 필수 입력 값입니다.")
    private String username;
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email
    private String email;;
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    public Member toEntity() {
        Member member = Member.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
        return member;
    }

}
