package com.example.fooddelivery.service.user_service;

import com.example.fooddelivery.entity.Admin;
import com.example.fooddelivery.entity.Role;
import com.example.fooddelivery.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import com.example.fooddelivery.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public boolean register(User user) {
        boolean isUserExist = userRepository.findById(user.getId()).isPresent();

        if (!isUserExist) {
            this.userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public boolean login(String username, String password) {

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return user.checkPassword(password);
        }

        return false;
    }

    @Transactional
    @Override
    public void changeUserRole(User admin, User user, Role newRole) {
        if (!(admin instanceof Admin)) {
            throw new IllegalArgumentException("You have no permission to change roles!");
        }

        try {
            Class<Role> roleClass = (Class<Role>) newRole.getClass();

            Field roleNameField = roleClass.getDeclaredField("name");
            roleNameField.setAccessible(true);

            String newUserType = (String) roleNameField.get(newRole);

            user.setRole(newRole);
            userRepository.save(user);

            userRepository.updateUserType(user.getId(), newUserType);

            entityManager.clear();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error changing user role", e);
        }
    }
}