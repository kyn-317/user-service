package com.kyn.user.base.exception;

import org.springframework.http.HttpStatus;


public class UserNotFoundException extends GeneralException {
    public UserNotFoundException() {
        super("user not found", HttpStatus.NOT_FOUND,404);
    }
}