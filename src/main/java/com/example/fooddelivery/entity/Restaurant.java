package com.example.fooddelivery.entity;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Restaurants")
public class Restaurant extends IdEntity {

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "restaurant", targetEntity = Order.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders;

    @OneToMany(mappedBy = "restaurant", targetEntity = Product.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> products;

    @ManyToMany(mappedBy = "restaurants", targetEntity = Cuisine.class, cascade = CascadeType.ALL)
    private Set<Cuisine> cuisines;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    public Restaurant() {
        this.isActive = true;
        this.orders = new HashSet<>();
        this.products = new HashSet<>();
        this.cuisines = new HashSet<>();
    }

    public Restaurant(String name, Address address) {
        this.name = name;
        this.address = address;
        this.isActive = true;
        this.orders = new HashSet<>();
        this.products = new HashSet<>();
        this.cuisines = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void addAddress(Address address) {
        if (this.address == null) {
            this.address = address;
            address.setRestaurant(this);
        }
    }

    public void removeAddress() {
        if (this.address != null) {
            this.address.setRestaurant(null);
            this.address = null;
        }
    }

    public Set<Order> getOrders() {
        return Collections.unmodifiableSet(this.orders);
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        order.setRestaurant(this);
    }

    public void removeOrder(Order order) {
        this.orders.remove(order);
        order.setRestaurant(null);
    }

    public Set<Product> getProducts() {
        return Collections.unmodifiableSet(this.products);
    }

    public void addProduct(Product product) {
        if (product != null && this.products.add(product)) {
            product.setRestaurant(this);
        }
    }

    public void removeProduct(Product product) {
        if (product != null && this.products.remove(product)) {
            product.setRestaurant(null);
        }
    }

    public Set<Cuisine> getCuisines() {
        return Collections.unmodifiableSet(this.cuisines);
    }

    public void addCuisine(Cuisine cuisine) {
        if (cuisine != null && this.cuisines.add(cuisine)) {
            cuisine.addRestaurant(this);
        }
    }

    public void removeCuisine(Cuisine cuisine) {
        if (cuisine != null && this.cuisines.remove(cuisine)) {
            cuisine.removeRestaurant(this);
        }
    }
}