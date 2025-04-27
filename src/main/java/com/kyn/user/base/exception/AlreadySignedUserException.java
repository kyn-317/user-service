package com.kyn.user.base.exception;

import org.springframework.http.HttpStatus;

public class AlreadySignedUserException extends GeneralException{

    public AlreadySignedUserException() {
        super("already signed user", HttpStatus.CONFLICT, 409);
    }
    
}
