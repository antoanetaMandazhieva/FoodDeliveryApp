package com.example.fooddelivery.dto.cuisine;

public class CuisineDto {

    private Long id;
    private String name;

    public CuisineDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        CuisineDto cuisineDto = (CuisineDto) o;

        return this.id.equals(cuisineDto.getId());
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}