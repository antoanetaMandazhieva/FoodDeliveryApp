package com.example.fooddelivery.exception.role;

import com.example.fooddelivery.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidRoleException extends BaseException {

    public InvalidRoleException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}