package com.example.fooddelivery.service.discount;

import com.example.fooddelivery.entity.discount.Discount;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.enums.DiscountType;
import com.example.fooddelivery.exception.discount.InvalidDiscountUserException;
import com.example.fooddelivery.repository.DiscountRepository;
import com.example.fooddelivery.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.example.fooddelivery.util.SystemErrors.Discount.NOT_CLIENT_ROLE_FOR_DISCOUNT;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final UserService userService;
    private final DiscountRepository discountRepository;

    public DiscountServiceImpl(UserService userService, DiscountRepository discountRepository) {
        this.userService = userService;
        this.discountRepository = discountRepository;
    }

    @Override
    @Transactional
    public Discount checkAndGiveClientDiscount(User client) {

        if (!"CLIENT".equals(client.getRole().getName())) {
            throw new InvalidDiscountUserException(NOT_CLIENT_ROLE_FOR_DISCOUNT);
        }

        int countOrders = userService.getOrdersByClientUsername(client.getUsername()).size();

        if (countOrders % 10 == 0) {
            Discount discount = new Discount();

            BigDecimal discountAmount = BigDecimal.valueOf(0.1);

            discount.setDiscountAmount(discountAmount);
            discount.setDiscountType(DiscountType.CLIENT);
            client.addDiscount(discount);


            return discountRepository.save(discount);
        }

        return null;
    }

    @Override
    public Discount checkAndGiveWorkerDiscount(User worker) {

        String role = worker.getRole().getName();

        BigDecimal discountAmount = BigDecimal.ZERO;
        DiscountType discountType = null;

        switch (role) {
            case "ADMIN" -> {
                discountAmount = BigDecimal.valueOf(0.3);
                discountType = DiscountType.ADMIN;
            }
            case "EMPLOYEE" -> {
                discountAmount = BigDecimal.valueOf(0.2);
                discountType = DiscountType.EMPLOYEE;
            }
            case "SUPPLIER" -> {
                discountAmount = BigDecimal.valueOf(0.1);
                discountType = DiscountType.SUPPLIER;
            }
        }

        Discount discount = new Discount();

        discount.setDiscountAmount(discountAmount);
        discount.setDiscountType(discountType);
        worker.addDiscount(discount);

        return discountRepository.save(discount);
    }
}