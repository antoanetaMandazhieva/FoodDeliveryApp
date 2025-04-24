package com.example.fooddelivery.exception.auth;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends AuthException {

    public UserAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}