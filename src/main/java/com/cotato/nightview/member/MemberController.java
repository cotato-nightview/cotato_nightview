package com.cotato.nightview.member;

import com.cotato.nightview.exception.ExceptionMessage;
import com.cotato.nightview.exception.InvalidLocationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberServiceImpl memberService;

    @GetMapping("/signup")
    public String signupForm() {
        return "/member/createMemberForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute MemberDto memberDto, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("memberDto", memberDto);
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "/member/createMemberForm";
        }
        memberService.saveMember(memberDto);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "/member/loginMemberForm";
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public String duplicateMember(IllegalArgumentException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message",e.getMessage());
        return "redirect:/signup";
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
