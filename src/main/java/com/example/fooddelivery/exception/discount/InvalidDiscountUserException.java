package com.example.fooddelivery.exception.discount;

import com.example.fooddelivery.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidDiscountUserException extends BaseException {

    public InvalidDiscountUserException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}