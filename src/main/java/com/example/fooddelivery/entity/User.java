package com.example.fooddelivery.entity;

import com.example.fooddelivery.enums.Gender;
import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.example.fooddelivery.util.Messages.*;

@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public abstract class User extends IdEntity {

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 60, nullable = false)
    private String password;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "user", targetEntity = Address.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses;

    @OneToMany(mappedBy = "user", targetEntity = Bonus.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bonus> bonuses;

    @OneToMany(mappedBy = "client", targetEntity = Order.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders;

    @Column(name = "phone_num", nullable = false)
    private String phoneNumber;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public User () {
        this.isActive = true;
        this.addresses = new HashSet<>();
        this.bonuses = new HashSet<>();
        this.orders = new HashSet<>();
    }

    public User(String email, String password, String username,
                String name, String surname, Gender gender, int day, int month, int year,
                String phoneNumber) {

        setEmail(email);
        setPassword(password);
        setUsername(username); // TODO При свързване на БД трябва да се валидира
        setName(name);
        setSurname(surname);
        setGender(gender);
        setDateOfBirth(day, month, year);
        setPhoneNumber(phoneNumber);
        this.isActive = true;
        this.addresses = new HashSet<>();
        this.bonuses = new HashSet<>();
        this.orders = new HashSet<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!email.matches("^[a-zA-Z0-9+&*-]+\\.[a-zA-Z0-9+&-]+@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new IllegalArgumentException(INVALID_EMAIL);
        }

        this.email = email;
    }

    public void setPassword(String password) {
        if (password.length() < 8 || !password.matches(".*[a-zA-Z]+.*")) {
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(int day, int month, int year) {
        try {
            this.dateOfBirth = LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(INVALID_DATE);
        }
    }

    public Set<Address> getAddresses() {
        return Collections.unmodifiableSet(this.addresses);
    }

    public Set<Bonus> getBonuses() {
        return Collections.unmodifiableSet(this.bonuses);
    }

    public Set<Order> getOrders() {
        return Collections.unmodifiableSet(this.orders);
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

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    private void validateName(String name) {
        if (!name.matches("^[A-Z][a-z]+$")) {
            throw new IllegalArgumentException(INVALID_NAME);
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(Address address) {
        this.addresses.remove(address);
        address.setUser(null);
    }

    public void addBonus(Bonus bonus) {
        this.bonuses.add(bonus);
        bonus.setUser(this);
    }

    public void removeBonus(Bonus bonus) {
        this.bonuses.remove(bonus);
        bonus.setUser(null);
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        order.setClient(this);
    }

    public void removeOrder(Order order) {
        this.orders.remove(order);
        order.setClient(null);
    }
}