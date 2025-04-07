package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.SupplierReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierReviewRepository extends JpaRepository<SupplierReview, Long> {

}