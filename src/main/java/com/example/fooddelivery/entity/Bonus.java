package com.example.fooddelivery.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Bonuses")
public class Bonus extends IdEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "bonus_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "bonus_date_time")
    private LocalDateTime bonusDateTime;

    public Bonus() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getBonusDateTime() {
        return bonusDateTime;
    }

    public void setBonusDateTime(LocalDateTime bonusDateTime) {
        this.bonusDateTime = bonusDateTime;
    }
}