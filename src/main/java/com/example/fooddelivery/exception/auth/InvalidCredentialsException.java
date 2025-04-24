package com.example.fooddelivery.exception.auth;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends AuthException {

    public InvalidCredentialsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}