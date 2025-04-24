package com.example.fooddelivery.exception.role;

import com.example.fooddelivery.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends BaseException {

    public RoleNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
