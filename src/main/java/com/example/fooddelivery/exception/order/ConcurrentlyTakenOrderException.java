package com.example.fooddelivery.exception.order;

import com.example.fooddelivery.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class ConcurrentlyTakenOrderException extends BaseException {

    public ConcurrentlyTakenOrderException(String message, Throwable cause) {
        super(message, cause, HttpStatus.CONFLICT);
    }
}
