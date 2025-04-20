package com.example.fooddelivery.entity.id_mapped_superclass;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/** <h4>
 * Клас, който се наследява от всички Entity-та, които се пазят в таблица.
 * </h4>
 * <p>
 *   Функцията е, че пести повторение на код и папка {@code entity} е по-стрктурирана.
 * </p>
 */

@MappedSuperclass
public class IdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public IdEntity() {}

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        IdEntity idEntity = (IdEntity) object;
        return id == idEntity.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}