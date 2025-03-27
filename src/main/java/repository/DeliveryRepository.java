package repository;

import entity.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeliveryRepository {
    private static volatile DeliveryRepository instance;

    private List<Order> deliveries;

    private DeliveryRepository() {
        this.deliveries = new ArrayList<>();
    }

    public static DeliveryRepository getInstance() {
        if (instance == null) {
            synchronized (DeliveryRepository.class) {
                if (instance == null) {
                    instance = new DeliveryRepository();
                }
            }
        }

        return instance;
    }

    public void addDelivery(Order delivery) {
        this.deliveries.add(delivery);
    }

    public Order getById(long id) {

        Order delivery = this.deliveries.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);

        if (delivery == null) {
            throw new IllegalArgumentException(String.format("No delivery with ID: %d", id));
        }

        return delivery;
    }

    /*public void removeDelivery(Order delivery) {
        // TODO
        *//**
         * Estimated time AND Delivery time LOGIC!
         *//*
        if (delivery.getStatus() == DeliveryStatus.DELIVERED || delivery.getStatus() == DeliveryStatus.FAILED) {
            this.deliveries.remove(delivery);
        }
    }*/



    public List<Order> getAllDeliveries() {
        return Collections.unmodifiableList(this.deliveries);
    }
}