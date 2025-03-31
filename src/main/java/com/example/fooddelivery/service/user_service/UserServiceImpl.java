package com.example.fooddelivery.service.user_service;

import com.example.fooddelivery.entity.User;
import org.springframework.stereotype.Service;
import com.example.fooddelivery.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void register(User user) {
        this.userRepository.save(user);
    }
}