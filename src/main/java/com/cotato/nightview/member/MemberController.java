package com.cotato.nightview.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberServiceImpl userService;

    @GetMapping("/signup")
    public String signupForm() {
        System.out.println("dfsfasfsdfasfsafd");
        return "/member/createMemberForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute MemberDto memberDto, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("memberDto", memberDto);
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "/member/createMemberForm";
        }
        userService.saveMember(memberDto);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "/member/loginMemberForm";
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
