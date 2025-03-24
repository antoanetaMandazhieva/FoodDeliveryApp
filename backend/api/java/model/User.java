package model;

import enums.Gender;
import org.mindrot.jbcrypt.BCrypt;

import java.text.SimpleDateFormat;
import java.util.Date;

import static util.Messages.*;


// TODO

/**
 * Трябва да се валидират email
 */

public abstract class User {

    private final static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    private static int INITIAL_ID = 1;


    private long id;
    private String email;
    private String password;
    private String username;
    private String name;
    private String surname;
    private Gender gender;
    private Date dateOfBirth;
    private String address;
    private String phoneNumber;
    private boolean isActive;

    public User(long id, String email, String password, String username,
                String name, String surname, Gender gender, Date dateOfBirth,
                String address, String phoneNumber) {

        setId(id);
        setEmail(email);
        setPassword(password);
        setUsername(username); // TODO При свързване на БД трябва да се валидира
        setName(name);
        setSurname(surname);
        setGender(gender);
        setDateOfBirth(dateOfBirth);
        setAddress(address);
        setPhoneNumber(phoneNumber);
        this.isActive = true;
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = INITIAL_ID++;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!email.matches( "^[a-zA-Z0-9+&*-]+(?:\\.[a-zA-Z0-9+&-]+)@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new IllegalArgumentException(INVALID_EMAIL);
        }

        this.email = email;
    }

    private void setPassword(String password) {
        if (password.length() < 8 && !password.matches(".*[a-zA-Z]+.*")) {
            throw new IllegalArgumentException(WRONG_PASSWORD);
        }

        this.password = hashPassword(password);
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);

        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        validateName(surname);

        this.surname = surname;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        Date today = new Date();
        if (dateOfBirth.after(today)) {
            throw new IllegalArgumentException(INVALID_DATE_IN_THE_FUTURE);
        }

        long maxAgeInMillis = 1000L * 60 * 60 * 24 * 365 * 100;
        Date earliestValidDate = new Date(today.getTime() - maxAgeInMillis);
        if (dateOfBirth.before(earliestValidDate)) {
            throw new IllegalArgumentException(MORE_THAN_100_YEARS);
        }

        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches( "^(\\+359|0)\\d{9}$")) {
            throw new IllegalArgumentException(INVALID_PHONE_NUMBER);
        }

        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    private void validateName(String name) {
        if (!name.matches("^[A-Z][a-z]+$")) {
            throw new IllegalArgumentException(INVALID_NAME);
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}