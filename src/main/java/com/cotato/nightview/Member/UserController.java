package com.cotato.nightview.Member;

import com.cotato.nightview.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
//@RestController
@Controller
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    private final UserService userService;

    //회원가입 화면, signup링크로 들어와서 리턴에 있는 것 보여줌(templates 아래에 있는 html 파일이 반환값)
    //html 파일에서 action이 링크

    //Model model 안해주면 restcontroller에서 그냥 반환값 문자열로 줘버림

    @GetMapping(value = "/signup")
    public String createSignupForm(Model model){
        return "member/createMemberForm";
    }

    // 회원가입

    /*
    @PostMapping("/signup")
    public Long join(@RequestBody Map<String, String> user) {
        return memberRepository.save(Member.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .membername(user.get("membername"))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()).getMember_id();
    }
    */

    @PostMapping("/signup")
    public ResponseEntity<?> join(@Valid @RequestParam UserDto userDto) {


        return userService.saveUser(userDto);
    }

    /*
    @PostMapping("/signup")
    public ResponseEntity<Long> register(@RequestBody UserDto userDto) { // 회원 추가
        Long registerId = userService.register(userDto);
        return ResponseEntity.created(URI.create("/signup/" + registerId)).body(registerId);
    }
    */


    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        Member member = memberRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }

    //
    //위에랑 이거랑 결합해야함
    @Override
    public ResponseEntity login(LoginDto loginDto) {

        Optional<Member> member = memberRepository.findByEmail(loginDto.getEmail());
        Member memberEntity = member.orElse(null);

        if (member==null){
            return new ResponseEntity("해당 아이디를 가진 회원이 존재하지 않습니다.", HttpStatus.OK);
        }

        if (memberEntity.getPassword().equals(loginDto.getPassword())){
            return new ResponseEntity("success", HttpStatus.OK);
        } else {
            return new ResponseEntity("비밀번호가 일치하지 않습니다.", HttpStatus.OK);
        }

    }


}
