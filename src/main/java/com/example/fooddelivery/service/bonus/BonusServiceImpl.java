package com.example.fooddelivery.service.bonus;

import com.example.fooddelivery.config.bonus.BonusMapper;
import com.example.fooddelivery.dto.bonus.BonusDto;
import com.example.fooddelivery.entity.Bonus;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.enums.OrderStatus;
import com.example.fooddelivery.repository.BonusRepository;
import com.example.fooddelivery.repository.OrderRepository;
import com.example.fooddelivery.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BonusServiceImpl implements BonusService {

    private static final int BONUS_THRESHOLD = 10;
    private static final BigDecimal BONUS_AMOUNT = BigDecimal.valueOf(20.00);

    private final BonusRepository bonusRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BonusMapper bonusMapper;

    public BonusServiceImpl(BonusRepository bonusRepository,
                            OrderRepository orderRepository,
                            UserRepository userRepository,
                            BonusMapper bonusMapper) {
        this.bonusRepository = bonusRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.bonusMapper = bonusMapper;
    }

    @Override
    public void checkAndAddBonusForSupplier(User supplier) {
        long deliveredCount = orderRepository
                .findBySupplierId(supplier.getId())
                .stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.DELIVERED)
                .count();

        long bonusCount = bonusRepository.findByUserId(supplier.getId()).size();

        if ((deliveredCount / BONUS_THRESHOLD) > bonusCount) {
            Bonus bonus = new Bonus();

            bonus.setUser(supplier);
            bonus.setAmount(BONUS_AMOUNT);
            bonus.setBonusDateTime(LocalDateTime.now());

            bonusRepository.save(bonus);

        }

    }

    @Override
    public List<BonusDto> getBonusesByUser(Long userId) {
        return bonusRepository.findByUserId(userId).stream()
                .map(bonusMapper::toBonusDto)
                .toList();
    }

    @Override
    public List<BonusDto> getBonusesBetweenDates(Long userId, LocalDateTime from, LocalDateTime to) {
        return bonusRepository.findByUserIdAndBonusDateTimeBetween(userId, from, to)
                .stream()
                .map(bonusMapper::toBonusDto)
                .toList();
    }
}