package com.example.fooddelivery.entity.address;

import com.example.fooddelivery.entity.id_mapped_superclass.IdEntity;
import com.example.fooddelivery.entity.restaurant.Restaurant;
import com.example.fooddelivery.entity.user.User;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Addresses")
public class Address extends IdEntity {

    @Column(length = 100)
    private String street;

    @Column(length = 50)
    private String city;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(length = 50)
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "address", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Restaurant restaurant;

    public Address() {}

    public Address(String street, String city, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void addRestaurant(Restaurant restaurant) {
        if (this.restaurant == null) {
            this.restaurant = restaurant;
            this.restaurant.setAddress(this);
        }
    }

    public void removeRestaurant() {
        if (this.restaurant != null) {
            this.restaurant.setAddress(null);
            this.restaurant = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(this.street, address.getStreet()) &&
                Objects.equals(this.city, address.getCity()) &&
                Objects.equals(this.postalCode, address.getPostalCode()) &&
                Objects.equals(this.country, address.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, postalCode, country);
    }
}