package entity;

import jakarta.persistence.*;

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

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private Restaurant restaurant;

    public Address() {
        this.user = new Client();
    }

    public Address(String street, String city, String postalCode, String country, User user, Restaurant restaurant) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.user = user;
        this.restaurant = restaurant;
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
}