package com.example.fooddelivery.service.discount;

import com.example.fooddelivery.entity.User;

import java.math.BigDecimal;

public interface DiscountService {

    BigDecimal checkAndGiveClientDiscount(User client);

    BigDecimal checkAndGiveWorkerDiscount(User worker);
}