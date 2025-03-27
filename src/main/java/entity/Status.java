package entity;

import jakarta.persistence.*;

/**
 * Засега се оставя връзката между {@code Status} и {@code Order} да е еднопосочна, което означава, че в даден етап,
 * когато даден Статус иска да достъпи поръчките си ще стане
 * чрез JPQL заявка, а не директно през класа!
 * <p>
 * Важно!!!
 * Това остава така засега само заради по-трудното му управление, но в даден етап може да се промени, ако е нужно.
 */

@Entity
@Table(name = "Statuses")
public class Status extends IdEntity {

    @Column(name = "status_name" , length = 50, nullable = false, unique = true)
    private String statusName;

    public Status() {}

    public Status(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }


}