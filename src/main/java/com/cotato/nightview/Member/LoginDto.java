package com.cotato.nightview.Member;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto  {

    private String email;;
    private String password;

    public Member toEntity() {
        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();
        return member;
    }
}
