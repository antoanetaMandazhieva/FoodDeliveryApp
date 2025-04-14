package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BonusRepository extends JpaRepository<Bonus, Long> {

    List<Bonus> findByUserId(Long userId);

    long countByUserId(Long userId);

    List<Bonus> findByUserIdAndBonusDateTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);
}