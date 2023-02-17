package com.cotato.nightview.member;


import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceImpl implements MemberService {

    //ctrl + alt +l로 자동정렬
    //@autowired 쓰는 대신 @RequiredArgsConstructor + final 쓰는것이 더 용이
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //스프링 시큐리티 쓰게되면 아래코드 필요없음
//    @Transactional
//    public Member login(LoginDto loginDto) {
//        System.out.println("loginDto.getEmail() = " + loginDto.getEmail());
//        System.out.println("loginDto.getPassword() = " + loginDto.getPassword());
//        return memberRepository.findByEmail(loginDto.getEmail())
//                .filter(m -> m.getPassword().equals(loginDto.getPassword()))
//                .orElse(null);
//    }

    @Transactional
    public void saveMember(MemberDto memberDto) {
        validateDuplicateMember(memberDto);
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        memberRepository.save(memberDto.toEntity());
    }

    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    private void validateDuplicateMember(MemberDto memberDto) {
        if (memberRepository.existsByEmail(memberDto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
    }
}
