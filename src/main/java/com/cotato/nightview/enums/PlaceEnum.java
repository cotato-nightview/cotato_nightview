package com.cotato.nightview.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlaceEnum {
    TOURIST_ATTRACTION("명소"),
    PLACE_NAME("지명"),
    SEOUL("서울"),
    NIGHT_SCAPE("야경");
    private String keyword;
}
