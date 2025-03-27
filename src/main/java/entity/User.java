package entity;

import enums.Gender;
import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static util.Messages.*;

/**
 * Засега се оставя връзката между {@code User} и {@code Bonus} да е еднопосочна, което означава, че в даден етап,
 * когато дадена потребител иска да достъпи бонусите си ще стане
 * чрез JPQL заявка, а не директно през класа!
 * <p>
 * Важно!!!
 * Това остава така засега само заради по-трудното му управление, но в даден етап може да се промени, ако е нужно.
 * </p>
 */

@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public abstract class User extends IdEntity {

    private final static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 50, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String username;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses;

    @Column(name = "phone_num", nullable = false)
    private String phoneNumber;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public User () {
        this.isActive = true;
        this.roles = new HashSet<>();
    }

    public User(String email, String password, String username,
                String name, String surname, Gender gender, LocalDate dateOfBirth,
                Set<Address> addresses, String phoneNumber) {

        setEmail(email);
        setPassword(password);
        setUsername(username); // TODO При свързване на БД трябва да се валидира
        setName(name);
        setSurname(surname);
        setGender(gender);
        setDateOfBirth(dateOfBirth);
        setAddresses(addresses);
        setPhoneNumber(phoneNumber);
        this.isActive = true;
        this.roles = new HashSet<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!email.matches( "^[a-zA-Z0-9+&*-]+(?:\\.[a-zA-Z0-9+&-]+)@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new IllegalArgumentException(INVALID_EMAIL);
        }

        this.email = email;
    }

    public void setPassword(String password) {
        if (password.length() < 8 || !password.matches(".*[a-zA-Z]+.*")) {
            throw new IllegalArgumentException(WRONG_PASSWORD);
        }

        this.password = hashPassword(password);
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);

        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        validateName(surname);

        this.surname = surname;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        LocalDate today = LocalDate.now();
        if (dateOfBirth.isAfter(today)) {
            throw new IllegalArgumentException(INVALID_DATE_IN_THE_FUTURE);
        }

        LocalDate earliestValidDate = today.minusYears(100);
        if (dateOfBirth.isBefore(earliestValidDate)) {
            throw new IllegalArgumentException(MORE_THAN_100_YEARS);
        }

        this.dateOfBirth = dateOfBirth;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches( "^(\\+359|0)\\d{9}$")) {
            throw new IllegalArgumentException(INVALID_PHONE_NUMBER);
        }

        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(this.roles);
    }

    private void validateName(String name) {
        if (!name.matches("^[A-Z][a-z]+$")) {
            throw new IllegalArgumentException(INVALID_NAME);
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        User user = (User) o;
        return this.getId() == user.getId();
    }

    @Override
    public int hashCode()  {
        return Long.hashCode(this.getId());
    }
}