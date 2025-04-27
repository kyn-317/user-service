package com.kyn.user.base.exception;

import org.springframework.http.HttpStatus;



public class InvalidCredentialsException extends GeneralException {
    public InvalidCredentialsException() {
        super("Invalid Credentials", HttpStatus.UNAUTHORIZED,401);
    }
}