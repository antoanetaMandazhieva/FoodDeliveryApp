package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query(value =  "UPDATE Users " +
                    "SET user_type = :newType " +
                    "WHERE id = :userId", nativeQuery = true)
    void updateUserType(@Param("userId") Long userId, @Param("newType") String newType);

    Optional<User> findByUsername(String username);
}