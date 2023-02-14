package com.cotato.nightview.exception;

import lombok.Getter;

@Getter
public class InvaildLocationException extends RuntimeException {
    private String errorMessage;

    public InvaildLocationException(String errorMessage){
        this.errorMessage = errorMessage;
    }
}
