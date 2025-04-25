package com.example.fooddelivery.exception.order;

import com.example.fooddelivery.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class AlreadyTakenOrderException extends BaseException {

    public AlreadyTakenOrderException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
