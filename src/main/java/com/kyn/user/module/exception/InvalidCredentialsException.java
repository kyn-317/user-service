package com.kyn.user.module.exception;

import org.springframework.http.HttpStatus;

import com.kyn.user.base.exception.GeneralException;

public class InvalidCredentialsException extends GeneralException {
    public InvalidCredentialsException() {
        super("Invalid Credentials", HttpStatus.UNAUTHORIZED);
    }
}