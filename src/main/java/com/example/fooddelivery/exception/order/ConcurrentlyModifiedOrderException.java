package com.example.fooddelivery.exception.order;

import com.example.fooddelivery.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class ConcurrentlyModifiedOrderException extends BaseException {

    public ConcurrentlyModifiedOrderException(String message, Throwable cause) {
        super(message, cause, HttpStatus.CONFLICT);
    }
}
