package repository;

import enums.DeliveryStatus;
import model.Delivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeliveryRepository {
    private static volatile DeliveryRepository instance;

    private List<Delivery> deliveries;

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

    public void addDelivery(Delivery delivery) {
        this.deliveries.add(delivery);
    }

    public Delivery getById(long id) {

        Delivery delivery = this.deliveries.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);

        if (delivery == null) {
            throw new IllegalArgumentException(String.format("No delivery with ID: %d", id));
        }

        return delivery;
    }

    public void removeDelivery(Delivery delivery) {
        // TODO
        /**
         * Estimated time AND Delivery time LOGIC!
         */
        if (delivery.getStatus() == DeliveryStatus.DELIVERED || delivery.getStatus() == DeliveryStatus.FAILED) {
            this.deliveries.remove(delivery);
        }
    }



    public List<Delivery> getAllDeliveries() {
        return Collections.unmodifiableList(this.deliveries);
    }
}