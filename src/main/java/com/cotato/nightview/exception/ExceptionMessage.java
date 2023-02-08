package com.cotato.nightview.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionMessage {
    String message = "";
    String href = "";

    public ExceptionMessage(String message, String href) {
        this.message = message;
        this.href = href;
    }
}