package entity;

import jakarta.persistence.*;

/**
 * Засега се оставя връзката между {@code Address} и {@code Restaurant} да е еднопосочна, което означава, че в даден етап,
 *  * когато даден Адрес иска да достъпи Ресторанта си ще стане
 *  * чрез JPQL заявка, а не директно през класа!
 *  * <p>
 *  * Важно!!!
 *  * Това остава така засега само заради по-трудното му управление, но в даден етап може да се промени, ако е нужно.
 *  * </p>
 */

@Entity
@Table(name = "Addresses")
public class Address extends IdEntity {

    @Column(length = 100)
    private String street;

    @Column(length = 50)
    private String city;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(length = 50)
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Address() {}

    public Address(String street, String city, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Address address = (Address) o;
        return this.getId() == address.getId();
    }

}