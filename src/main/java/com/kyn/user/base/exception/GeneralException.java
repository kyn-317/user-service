package com.kyn.user.base.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {
    private final HttpStatus status;
    private final String message;
    private final int code;

    public GeneralException(String message, HttpStatus status, int code) {
        super(message);
        this.message = message;
        this.status = status;
        this.code = code;
    }
}