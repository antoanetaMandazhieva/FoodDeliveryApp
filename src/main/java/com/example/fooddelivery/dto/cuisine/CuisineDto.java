package com.example.fooddelivery.dto.cuisine;

public class CuisineDto {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public CuisineDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}