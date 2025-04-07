package com.example.fooddelivery.entity;

import com.example.fooddelivery.enums.Gender;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User {

    public Client() {
        super();
    }

    public Client(String email, String password, String username,
                  String name, String surname, Gender gender, int day, int month, int year,
                  String phoneNumber) {

        super(email, password, username, name, surname, gender, day, month, year, phoneNumber);
    }

}