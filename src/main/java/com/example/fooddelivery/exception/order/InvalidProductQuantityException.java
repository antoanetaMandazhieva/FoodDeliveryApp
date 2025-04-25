package com.example.fooddelivery.exception.order;

import com.example.fooddelivery.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidProductQuantityException extends BaseException {

    public InvalidProductQuantityException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
