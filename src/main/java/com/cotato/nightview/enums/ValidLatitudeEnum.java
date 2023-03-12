package com.cotato.nightview.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public enum ValidLatitudeEnum {
    UPPER_LIMIT(38.61),
    LOWER_LIMIT(33.11);
    private double latitude;
}
