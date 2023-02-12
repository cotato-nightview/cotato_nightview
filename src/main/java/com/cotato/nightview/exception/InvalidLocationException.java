package com.cotato.nightview.exception;

import lombok.Getter;

@Getter
public class InvalidLocationException extends RuntimeException {
    private String errorMessage;

    public InvalidLocationException(String errorMessage){
        this.errorMessage = errorMessage;
    }
}
