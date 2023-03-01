package com.cotato.nightview.member;


import com.cotato.nightview.enums.ExceptionMessageEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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
            throw new IllegalArgumentException(ExceptionMessageEnum.ALREADY_SIGNED_UP_EMAIL.getMessage());
        }
        if (memberRepository.existsByUsername(memberDto.getUsername())) {
            throw new IllegalArgumentException(ExceptionMessageEnum.ALREADY_SIGNED_UP_USER_NAME.getMessage());
        }
    }

}
