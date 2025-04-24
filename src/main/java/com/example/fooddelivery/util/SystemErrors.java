package com.example.fooddelivery.util;

/**
 * Съобщения при грешки
 */

public class SystemErrors {

    public static final class Auth {
        public static final String INVALID_USERNAME = "Invalid username.";
        public static final String INVALID_PASSWORD = "Invalid password.";
        public static final String USERNAME_ALREADY_TAKEN = "Username is already taken.";
        public static final String EMAIL_ALREADY_TAKEN = "Email is already taken.";
        public static final String PHONE_NUMBER_ALREADY_TAKEN = "Phone number is already taken.";
    }

    public static final class Role {
        public static final String NO_CLIENT_ROLE = "There is no CLIENT role!";
    }

    public static final class Review {
        public final static String INVALID_RATING = "Invalid rating.";
    }

    private SystemErrors() {}
}