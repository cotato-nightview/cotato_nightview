package com.cotato.nightview.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final MemberRepository memberRepository;
    private final UserServiceImpl userService;

    @GetMapping("/signup")
    public String signupForm(){
        return "member/createMemberForm";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute UserDto userDto){
        userService.saveUser(userDto);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "member/loginMemberForm";
    }


    //스프링 시큐리티 사용시 미필요
//    @PostMapping("/login")
//    public String login(@ModelAttribute LoginDto loginDto, BindingResult bindingResult, HttpServletResponse httpServletResonse){
//        Member member = userService.login(loginDto);
//
//        if(member == null){
//            bindingResult.reject("loginfail", "아이디 혹은 비밀번호가 맞지 않습니다");
//            System.out.println("로그인 실패");
//            return "member/loginMemberForm";
//        }
//
//
//
//        return "redirect:/";
//    }


}
