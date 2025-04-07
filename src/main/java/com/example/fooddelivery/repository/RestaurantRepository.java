package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT DISTINCT r FROM Restaurant r " +
            "JOIN r.cuisines c " +
            "WHERE c.id = :cuisineId")
    List<Restaurant> findAllByCuisineId(@Param("cuisineId") Long cuisineId);

    Optional<Restaurant> findByName(String restaurantName);
}