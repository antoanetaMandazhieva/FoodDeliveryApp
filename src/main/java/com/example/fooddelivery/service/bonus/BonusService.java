package com.example.fooddelivery.service.bonus;

import com.example.fooddelivery.dto.bonus.BonusDto;
import com.example.fooddelivery.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BonusService {

    void checkAndAddBonusForSupplier(User supplier);

    List<BonusDto> getBonusesByUser(Long userId);

    List<BonusDto> getBonusesBetweenDates(Long userId, LocalDateTime from, LocalDateTime to);
}