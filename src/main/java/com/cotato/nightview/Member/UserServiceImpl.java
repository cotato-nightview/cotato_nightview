package com.cotato.nightview.Member;


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
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

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
    public void saveUser(UserDto userDto){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        memberRepository.save(userDto.toEntity());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> findEmail = memberRepository.findByEmail(email);
        Member member = findEmail.get();

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (("admin").equals(email)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
        }

        return new User(member.getEmail(), member.getPassword(), authorities);
    }




}
