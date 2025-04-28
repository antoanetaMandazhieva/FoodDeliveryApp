package com.example.fooddelivery.exception.user;

import com.example.fooddelivery.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class FieldAlreadyTakenException extends BaseException {

    public FieldAlreadyTakenException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}