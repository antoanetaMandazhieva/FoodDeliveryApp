package entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * <p>Може да се добави:</p>
 * <p>@OneToMany(mappedBy = "product")
 * private Set<OrderedItem> orderedItems = new HashSet<>();</p>
 * <p>Но само ако ще е нужна двупосочност.</p>
 */

@Entity
@Table(name = "Products")
public class Product extends IdEntity{

    @Column(length = 50, nullable = false)
    private String name;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "is_available", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isAvailable;

    public Product() {
        this.isAvailable = true;
    }

    public Product(String name, BigDecimal price, Category category, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.restaurant = restaurant;
        this.isAvailable = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return this.getId() == product.getId();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }
}