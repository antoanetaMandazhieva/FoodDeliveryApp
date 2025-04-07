package com.example.fooddelivery.entity;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Cuisines")
public class Cuisine extends IdEntity {

    @Column(length = 50)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "cuisine_restaurants",
            joinColumns = @JoinColumn(name = "cuisine_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    private Set<Restaurant> restaurants;

    @OneToMany(mappedBy = "cuisine", targetEntity = Product.class, orphanRemoval = true)
    private Set<Product> products;

    public Cuisine() {
        this.restaurants = new HashSet<>();
        this.products = new HashSet<>();
    }

    public Cuisine(String name) {
        this.name = name;
        this.restaurants = new HashSet<>();
        this.products = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return Collections.unmodifiableSet(this.products);
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
        product.setCuisine(this);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
        product.setCuisine(null);
    }

    public Set<Restaurant> getRestaurants() {
        return Collections.unmodifiableSet(this.restaurants);
    }

    public void addRestaurant(Restaurant restaurant) {
        this.restaurants.add(restaurant);
    }

    public void removeRestaurant(Restaurant restaurant) {
        this.restaurants.remove(restaurant);
    }
}