package com.example.fooddelivery.entity;

import com.example.fooddelivery.enums.Gender;
import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.example.fooddelivery.util.Messages.*;

@Entity
@Table(name = "Users")
public class User extends IdEntity {

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

    @OneToMany(mappedBy = "user", targetEntity = Address.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Address> addresses;

    @OneToMany(mappedBy = "user", targetEntity = Discount.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Discount> discounts;

    @OneToMany(mappedBy = "client", targetEntity = Order.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Order> orders;

    @Column(name = "phone_num", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public User () {
        this.isActive = true;
        this.addresses = new HashSet<>();
        this.discounts = new HashSet<>();
        this.orders = new HashSet<>();
    }

    public User(String email, String password, String username,
                String name, String surname, Gender gender, LocalDate dateOfBirth,
                String phoneNumber) {

        setEmail(email);
        setPassword(password);
        setUsername(username);
        setName(name);
        setSurname(surname);
        setGender(gender);
        setDateOfBirth(dateOfBirth);
        setPhoneNumber(phoneNumber);
        this.isActive = true;
        this.addresses = new HashSet<>();
        this.discounts = new HashSet<>();
        this.orders = new HashSet<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
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

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isOver18() {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }

        LocalDate today = LocalDate.now();

        return !today.isBefore(this.dateOfBirth.plusYears(18));
    }

    public Set<Address> getAddresses() {
        return Collections.unmodifiableSet(this.addresses);
    }

    public Set<Discount> getBonuses() {
        return Collections.unmodifiableSet(this.discounts);
    }

    public Set<Order> getOrders() {
        return Collections.unmodifiableSet(this.orders);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
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

    public void addBonus(Discount discount) {
        this.discounts.add(discount);
        discount.setUser(this);
    }

    public void removeBonus(Discount discount) {
        this.discounts.remove(discount);
        discount.setUser(null);
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        order.setClient(this);
    }

    public void removeOrder(Order order) {
        this.orders.remove(order);
        order.setClient(null);
    }

    public void detachAllRelations() {
        this.addresses.forEach(address -> address.setUser(null));
        this.discounts.forEach(discount -> discount.setUser(null));
        this.orders.forEach(order -> order.setClient(null));
    }
}