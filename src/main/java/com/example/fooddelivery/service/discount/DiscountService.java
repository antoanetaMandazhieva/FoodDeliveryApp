package com.example.fooddelivery.service.discount;

import com.example.fooddelivery.entity.discount.Discount;
import com.example.fooddelivery.entity.user.User;

import java.math.BigDecimal;

public interface DiscountService {

    Discount checkAndGiveClientDiscount(User client);

    Discount checkAndGiveWorkerDiscount(User worker);
}