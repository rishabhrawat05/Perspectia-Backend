package com.perspectia.perspectiabackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyExistsByEmailException extends RuntimeException {

    public UserAlreadyExistsByEmailException(String message){
        super(message);
    }
}
