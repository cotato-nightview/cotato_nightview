package com.cotato.nightview.Member;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public ResponseEntity saveUser(UserDto userDto) {

        Optional<Member> member = memberRepository.findByEmail(userDto.getEmail());

        if (member.isEmpty()) {
            Member newMember = Member.builder()
                    .email(userDto.getEmail())
                    .password(userDto.getPassword())
                    .membername(userDto.getMembername())
                    .build();

            memberRepository.save(newMember);

            return new ResponseEntity("success", HttpStatus.OK);
        } else {
            return new ResponseEntity("fail", HttpStatus.OK);
        }

    }
}
