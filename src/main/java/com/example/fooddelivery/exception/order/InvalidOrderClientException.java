package com.example.fooddelivery.exception.order;

import com.example.fooddelivery.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidOrderClientException extends BaseException {

    public InvalidOrderClientException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
