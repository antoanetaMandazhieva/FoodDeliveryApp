package app;

import entity.*;
import enums.Gender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Стартира сървъра
 */

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("sap-unit");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();


        em.getTransaction().commit();
        em.close();
    }

    public static void setClient(User client) {
        client.setEmail("client.123@gmail.com");
        client.setPassword("Ab123456");
        client.setUsername("client1234");
        client.setName("Ivan");
        client.setSurname("Ivanov");
        client.setGender(Gender.MALE);
        client.setDateOfBirth(11, 3, 2000);
        client.setPhoneNumber("0888991223");
    }

    public static void setSupplier(Supplier supplier) {
        supplier.setEmail("supplier.123@gmail.com");
        supplier.setPassword("SSS123456");
        supplier.setUsername("supplier1234");
        supplier.setName("Milena");
        supplier.setSurname("Todorova");
        supplier.setGender(Gender.FEMALE);
        supplier.setDateOfBirth(10, 2, 1998);
        supplier.setPhoneNumber("0888987654");
    }
}