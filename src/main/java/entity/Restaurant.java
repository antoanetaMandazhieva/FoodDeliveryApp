package entity;

import jakarta.persistence.*;

/**
 * Засега се оставя връзката между {@code Restaurant} и {@code Order} да е еднопосочна, което означава, че в даден етап,
 * когато даден Ресторант иска да достъпи поръчките си ще стане
 * чрез JPQL заявка, а не директно през класа!
 * <p>
 * Важно!!!
 * Това остава така засега само заради по-трудното му управление, но в даден етап може да се промени, ако е нужно.
 * </p>
 *
 * Засега се оставя връзката между {@code Restaurant} и {@code Product} да е еднопосочна, което означава, че в даден етап,
 * когато даден Ресторант иска да достъпи продуктите си ще стане
 * чрез JPQL заявка, а не директно през класа!
 * <p>
 * Важно!!!
 * Това остава така засега само заради по-трудното му управление, но в даден етап може да се промени, ако е нужно.
 * </p>
 */

@Entity
@Table(name = "Restaurants")
public class Restaurant extends IdEntity {

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    public Restaurant() {}

    public Restaurant(String name, Address address) {
        this.name = name;
        this.address = address;
        this.isActive = true;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}