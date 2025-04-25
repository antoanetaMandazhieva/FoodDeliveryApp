package com.example.fooddelivery.entity.discount;

import com.example.fooddelivery.entity.id_mapped_superclass.IdEntity;
import com.example.fooddelivery.entity.order.Order;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.enums.DiscountType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Discounts")
public class Discount extends IdEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "discount", targetEntity = Order.class)
    private Set<Order> orders;

    @Column(name = "discount_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal discountAmount;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    public Discount() {
        this.orders = new HashSet<>();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        if (order != null && this.orders.add(order)) {
            order.addDiscount(this);
        }
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }
}