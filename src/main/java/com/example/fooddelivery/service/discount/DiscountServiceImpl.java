package com.example.fooddelivery.service.discount;

import com.example.fooddelivery.config.discount.DiscountMapper;
import com.example.fooddelivery.entity.Discount;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.repository.DiscountRepository;
import com.example.fooddelivery.repository.OrderRepository;
import com.example.fooddelivery.repository.UserRepository;
import com.example.fooddelivery.service.user.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final UserService userService;
    private final DiscountRepository discountRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final DiscountMapper discountMapper;

    public DiscountServiceImpl(UserService userService,
                               DiscountRepository discountRepository,
                               OrderRepository orderRepository,
                               UserRepository userRepository,
                               DiscountMapper discountMapper) {
        this.userService = userService;
        this.discountRepository = discountRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.discountMapper = discountMapper;
    }


    @Override
    public BigDecimal checkAndGiveClientDiscount(User client) {

        if (!"CLIENT".equals(client.getRole().getName())) {
            throw new IllegalStateException("You cannot have client discount");
        }

        int countOrders = userService.getOrdersByClientUsername(client.getUsername()).size();

        if (countOrders % 10 == 0) {
            Discount discount = new Discount();

            BigDecimal discountAmount = BigDecimal.valueOf(0.1);

            discount.setDiscountAmount(discountAmount);
            discount.setUser(client);

            discountRepository.save(discount);

            return discountAmount;
        }

        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal checkAndGiveWorkerDiscount(User worker) {

        String role = worker.getRole().getName();

        BigDecimal discountAmount = BigDecimal.ZERO;

        switch (role) {
            case "ADMIN" -> discountAmount = BigDecimal.valueOf(0.3);
            case "EMPLOYEE" -> discountAmount = BigDecimal.valueOf(0.2);
            case "SUPPLIER" -> discountAmount = BigDecimal.valueOf(0.1);
        }

        Discount discount = new Discount();

        discount.setUser(worker);
        discount.setDiscountAmount(discountAmount);

        discountRepository.save(discount);

        return discountAmount;
    }
}