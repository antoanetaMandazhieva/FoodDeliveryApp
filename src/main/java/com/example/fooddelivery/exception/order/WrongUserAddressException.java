package com.example.fooddelivery.exception.order;

import com.example.fooddelivery.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class WrongUserAddressException extends BaseException {

    public WrongUserAddressException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}