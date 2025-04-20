package com.example.fooddelivery.service.discount;

import com.example.fooddelivery.config.discount.DiscountMapper;
import com.example.fooddelivery.dto.discount.DiscountDto;
import com.example.fooddelivery.entity.discount.Discount;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.repository.DiscountRepository;
import com.example.fooddelivery.repository.OrderRepository;
import com.example.fooddelivery.repository.UserRepository;
import com.example.fooddelivery.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Discount checkAndGiveClientDiscount(User client) {

        if (!"CLIENT".equals(client.getRole().getName())) {
            throw new IllegalStateException("You cannot have client discount");
        }

        int countOrders = userService.getOrdersByClientUsername(client.getUsername()).size();

        if (countOrders % 10 == 0) {
            Discount discount = new Discount();

            BigDecimal discountAmount = BigDecimal.valueOf(0.1);

            discount.setDiscountAmount(discountAmount);
            client.addDiscount(discount);


            return discountRepository.save(discount);
        }

        return null;
    }

    @Override
    public Discount checkAndGiveWorkerDiscount(User worker) {

        String role = worker.getRole().getName();

        BigDecimal discountAmount = BigDecimal.ZERO;

        switch (role) {
            case "ADMIN" -> discountAmount = BigDecimal.valueOf(0.3);
            case "EMPLOYEE" -> discountAmount = BigDecimal.valueOf(0.2);
            case "SUPPLIER" -> discountAmount = BigDecimal.valueOf(0.1);
        }

        Discount discount = new Discount();

        discount.setDiscountAmount(discountAmount);
        worker.addDiscount(discount);

        return discountRepository.save(discount);
    }
}