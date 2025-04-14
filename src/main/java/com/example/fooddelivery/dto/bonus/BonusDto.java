package com.example.fooddelivery.dto.bonus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BonusDto {

    private BigDecimal amount;
    private LocalDateTime bonusDateTime;

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