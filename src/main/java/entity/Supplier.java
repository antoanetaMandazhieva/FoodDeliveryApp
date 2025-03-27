package entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


/**
 * Засега се оставя връзката между {@code Supplier} и {@code Order} да е еднопосочна, което означава, че в даден етап,
 * когато даден Доставчик иска да достъпи поръчките си ще стане
 * чрез JPQL заявка, а не директно през класа!
 * <p>
 * Важно!!!
 * Това остава така засега само заради по-трудното му управление, но в даден етап може да се промени, ако е нужно.
 */

@Entity
@DiscriminatorValue("SUPPLIER")
public class Supplier extends User {




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