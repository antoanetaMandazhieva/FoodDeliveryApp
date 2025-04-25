package com.example.fooddelivery.exception.review;

import com.example.fooddelivery.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class SameClientAndSupplierException extends BaseException {

    public SameClientAndSupplierException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
