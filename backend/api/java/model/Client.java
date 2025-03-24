package model;

import enums.Gender;

import java.util.Date;

public class Client extends User {

    public Client(long id, String email, String password, String username,
                  String name, String surname, Gender gender, Date dateOfBirth,
                  String address, String phoneNumber) {

        super(id, email, password, username, name, surname, gender, dateOfBirth, address, phoneNumber);
    }
}
