package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.SupplierReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierReviewRepository extends JpaRepository<SupplierReview, Long> {

    List<SupplierReview> findBySupplierId(Long supplierId);

    List<SupplierReview> findByReviewerId(Long reviewerId);
}