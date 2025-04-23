package com.example.fooddelivery.entity.ordered_item;

import com.example.fooddelivery.entity.order.Order;
import com.example.fooddelivery.entity.product.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ordered_items")
public class OrderedItem {

    @EmbeddedId
    private OrderedItemId id;

    @ManyToOne
    @MapsId("orderId")
    private Order order;

    @ManyToOne
    @MapsId("productId")
    private Product product;

    private int quantity;

    public OrderedItem() {}

    public OrderedItem(Order order, Product product, int quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.id = new OrderedItemId();
    }

    @PrePersist
    public void assignCompositeKey() {
        if (this.id == null) {
            this.id = new OrderedItemId();
        }

        if (this.id.getOrderId() == null && order != null && order.getId() != 0) {
            this.id.setOrderId(order.getId());
        }

        if (this.id.getProductId() == null && product != null && product.getId() != 0) {
            this.id.setProductId(product.getId());
        }
    }

    public OrderedItemId getId() {
        return id;
    }

    public void setId(OrderedItemId id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal calculateTotalPrice() {
        return this.product.getPrice().multiply(BigDecimal.valueOf(this.quantity));
    }
}