package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusRepository extends JpaRepository<Bonus, Long> {

}