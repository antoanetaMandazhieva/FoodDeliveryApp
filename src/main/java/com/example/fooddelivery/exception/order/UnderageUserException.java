package com.example.fooddelivery.exception.order;

import com.example.fooddelivery.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class UnderageUserException extends BaseException {
    public UnderageUserException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
