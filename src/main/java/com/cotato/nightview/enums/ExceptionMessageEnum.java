package com.cotato.nightview.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionMessageEnum {
    NOT_SUPPORTED_LOCATION("지원하지 않는 위치입니다."),
    NON_EXISTENT_AREA_NAME("존재하지 않는 지역 이름입니다!"),
    NON_EXISTENT_PLACE_NAME("존재하지 않는 장소입니다"),
    NON_EXISTENT_USER_NAME("존재하지 않는 사용자 이름입니다."),
    ALREADY_SIGNED_UP_EMAIL("이미 가입된 이메일입니다."),
    ALREADY_SIGNED_UP_USER_NAME("이미 가입된 회원 이름입니다.");

    private String message;
}
