package com.cotato.nightview.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ValidLongitudeEnum {
    UPPER_LIMIT(131.87),
    LOWER_LIMIT(124.6);
    private double longitude;
}
