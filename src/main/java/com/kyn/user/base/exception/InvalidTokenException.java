package com.kyn.user.base.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends GeneralException {
    public InvalidTokenException() {
        super("Invalid Token", HttpStatus.UNAUTHORIZED,401);
    }
}
