package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByStreetAndCityAndCountryAndUserId(String street, String city, String country, Long userId);
}