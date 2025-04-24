package com.example.fooddelivery.dto.auth;

public class LoginResponseDto {

    private Long id;
    private String role;
    private String message;

    public LoginResponseDto(Long id, String role, String message) {
        this.id = id;
        this.role = role;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}