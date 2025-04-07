package com.example.fooddelivery.service.user_service;

import com.example.fooddelivery.entity.Role;
import com.example.fooddelivery.entity.User;

public interface UserService {

    boolean register(User user);

    boolean login(String username, String password);

    void changeUserRole(User admin, User user, Role newRole);
}