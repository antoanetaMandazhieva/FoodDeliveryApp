package entity;

import jakarta.persistence.*;

/**
 * Засега се оставя връзката между {@code Category} и {@code Product} да е еднопосочна, което означава, че в даден етап,
 * когато дадена категория иска да достъпи продуктите си ще стане
 * чрез JPQL заявка, а не директно през класа!
 * <p>
 * Важно!!!
 * Това остава така засега само заради по-трудното му управление, но в даден етап може да се промени, ако е нужно.
 * </p>
 */

@Entity
@Table(name = "Categories")
public class Category extends IdEntity {

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    public Category() {}

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}