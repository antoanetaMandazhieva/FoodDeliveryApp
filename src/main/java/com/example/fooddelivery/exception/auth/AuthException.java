package com.example.fooddelivery.exception.auth;

import com.example.fooddelivery.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class AuthException extends BaseException {

    public AuthException(String message, HttpStatus status) {
        super(message, status);
    }
}