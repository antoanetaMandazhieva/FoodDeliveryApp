package com.example.fooddelivery.exception.restaurant;

import com.example.fooddelivery.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class ProductNotInRestaurantException extends BaseException {

    public ProductNotInRestaurantException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}