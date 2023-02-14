package com.cotato.nightview.member;


import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceImpl implements MemberService, UserDetailsService {

    //ctrl + alt +l로 자동정렬
    //@autowired 쓰는 대신 @RequiredArgsConstructor + final 쓰는것이 더 용이
    private final MemberRepository memberRepository;

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
    public void saveUser(MemberDto memberDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(memberDto.getPassword());
        memberDto.setPassword(encodedPassword);
        memberRepository.save(memberDto.toEntity());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (("admin").equals(email)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
        }
        return new User(member.getUsername(), member.getPassword(), authorities);
    }

}
