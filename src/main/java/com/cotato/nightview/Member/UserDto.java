package com.cotato.nightview.Member;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {      //requestdto -> 요청으로부터 정보를 받아 DB에 저장할 떄
    private String username;
    private String email;;
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
