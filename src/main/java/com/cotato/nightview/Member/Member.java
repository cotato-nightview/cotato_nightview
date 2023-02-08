package com.cotato.nightview.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor      //기본 생성자 생성(파라미터 X)
@AllArgsConstructor     //모든 필드 값을 파라미터로 받는 생성자 만듦
@Builder                //@Builder와 @NoArgsConstructor를 함께 쓰면 오류가 발생?   더 알아보자
@Entity //DB테이블과 일대일로 매칭되는 객체 단위, Entity객체의 인스턴스 하나가 테이블에서 하나의 레코드 값을 의미, JPA가 관리
public class Member implements UserDetails {

    @Id //pk 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue : 기본키를 자동 생성해주는 어노테이션
    //IDENTITY 전략은 기본 키 생성을 데이터베이스에 위임하는 전략
    private Long member_id;

    @Column
    private String membername;

    @Column
    private String email;

    @Column
    private String password;


    //여기 아래부터 UserDetails 관련
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
