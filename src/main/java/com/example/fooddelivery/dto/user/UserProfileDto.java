package com.example.fooddelivery.dto.user;

import com.example.fooddelivery.enums.Gender;

import java.time.LocalDate;

public class UserProfileDto {

    private String username;
    private String email;
    private String name;
    private String surname;
    private Gender gender;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    public UserProfileDto() {}

    public UserProfileDto(String username, String email, String name, String surname, Gender gender, String phoneNumber, LocalDate dateOfBirth) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}