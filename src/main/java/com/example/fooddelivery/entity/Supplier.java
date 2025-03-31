package com.example.fooddelivery.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("SUPPLIER")
public class Supplier extends User {

    @OneToMany(mappedBy = "supplier", targetEntity = Order.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> supplierOrders;


    public Supplier() {
        super();
        this.supplierOrders = new HashSet<>();
    }

    public Set<Order> getSupplierOrders() {
        return Collections.unmodifiableSet(this.supplierOrders);
    }

    public void addSupplierOrder(Order order) {
        this.supplierOrders.add(order);
        order.setSupplier(this);
    }

    public void removeSupplierOrder(Order order) {
        this.supplierOrders.remove(order);
        order.setSupplier(null);
    }

    /*public void changeStatus(Order delivery) {
        DeliveryStatus status = delivery.getStatus();

        DeliveryStatus newStatus = switch (status) {
            case AWAITING -> ASSIGNED;
            case ASSIGNED -> IN_TRANSITION;
            case IN_TRANSITION -> DELIVERED;
            default -> throw new IllegalArgumentException("Invalid status.");
        };

        delivery.setStatus(newStatus);
    }*/
}