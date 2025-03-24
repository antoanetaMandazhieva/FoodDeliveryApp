package model;

import enums.DeliveryStatus;
import enums.Gender;
import repository.DeliveryRepository;

import java.util.Date;

import static enums.DeliveryStatus.*;

/**
 * Доставчика може да променя статуса на поръчките
 */

public class Supplier extends User {

    private DeliveryRepository deliveryRepository;


    public Supplier(long id, String email, String password, String username,
                    String name, String surname, Gender gender, Date dateOfBirth,
                    String address, String phoneNumber) {

        super(id, email, password, username, name, surname, gender, dateOfBirth, address, phoneNumber);
    }


    public void changeStatus(Delivery delivery) {
        DeliveryStatus status = delivery.getStatus();

        DeliveryStatus newStatus = switch (status) {
            case AWAITING -> ASSIGNED;
            case ASSIGNED -> IN_TRANSITION;
            case IN_TRANSITION -> DELIVERED;
            default -> throw new IllegalArgumentException("Invalid status.");
        };

        delivery.setStatus(newStatus);
    }
}