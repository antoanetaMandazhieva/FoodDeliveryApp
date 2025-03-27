package entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Засега се оставя връзката между {@code Client} и {@code Order} да е еднопосочна, което означава, че в даден етап,
 * когато даден Клиент иска да достъпи поръчките си ще стане
 * чрез JPQL заявка, а не директно през класа!
 * <p>
 * Важно!!!
 * Това остава така засега само заради по-трудното му управление, но в даден етап може да се промени, ако е нужно.
 */

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User {

    public Client() {
        super();
    }

}