package com.example.fooddelivery.entity.order;

import com.example.fooddelivery.entity.discount.Discount;
import com.example.fooddelivery.entity.ordered_item.OrderedItem;
import com.example.fooddelivery.entity.product.Product;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.entity.address.Address;
import com.example.fooddelivery.entity.id_mapped_superclass.IdEntity;
import com.example.fooddelivery.entity.restaurant.Restaurant;
import com.example.fooddelivery.enums.OrderStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "Orders")
public class Order extends IdEntity {

    @Version
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private User supplier;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "order", targetEntity = OrderedItem.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderedItem> orderedItems;

    @ManyToOne
    @JoinColumn(name = "discount")
    private Discount discount;

    public Order () {
        this.totalPrice = BigDecimal.ZERO;
        this.orderedItems = new HashSet<>();
        this.orderStatus = OrderStatus.PENDING;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public User getSupplier() {
        return supplier;
    }

    public void setSupplier(User supplier) {
        this.supplier = supplier;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void calculateTotalPrice(BigDecimal discountAmount) {
        this.totalPrice = this.orderedItems.stream()
                .map(OrderedItem::calculateTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalPrice = totalPrice.multiply(BigDecimal.ONE.subtract(discountAmount));
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Set<OrderedItem> getOrderedItems() {
        return Collections.unmodifiableSet(this.orderedItems);
    }

    public void addOrderedItem(Product product, int quantity) {
        OrderedItem item = new OrderedItem(this, product, quantity);
        this.orderedItems.add(item);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void addDiscount(Discount discount) {
        this.discount = discount;
    }
}