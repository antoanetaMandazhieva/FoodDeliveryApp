package com.example.fooddelivery.config.common;

import org.modelmapper.ModelMapper;

public class Mapper {

    private static ModelMapper instance;

    private Mapper() {}


    public static ModelMapper getInstance() {
        if (instance == null) {
            instance = new ModelMapper();
        }

        return instance;
    }
}