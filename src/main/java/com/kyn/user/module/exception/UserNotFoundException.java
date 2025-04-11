package com.kyn.user.module.exception;

import org.springframework.http.HttpStatus;

import com.kyn.user.base.exception.GeneralException;

public class UserNotFoundException extends GeneralException {
    public UserNotFoundException() {
        super("user not found", HttpStatus.NOT_FOUND);
    }
}