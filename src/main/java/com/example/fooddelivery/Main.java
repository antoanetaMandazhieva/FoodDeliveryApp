package com.example.fooddelivery;

import com.example.fooddelivery.entity.*;
import com.example.fooddelivery.repository.CuisineRepository;
import com.example.fooddelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.fooddelivery.repository.RoleRepository;
import com.example.fooddelivery.service.user_service.UserService;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Main implements CommandLineRunner {

    private UserRepository userRepository;
    private UserService userService;
    private RoleRepository roleRepository;
    private CuisineRepository cuisineRepository;

    public Main(UserRepository userRepository, UserService userService, RoleRepository roleRepository, CuisineRepository cuisineRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.cuisineRepository = cuisineRepository;
    }

    @Override
    public void run(String... args) {

//
//        Role clientRole = roleRepository.findById(2L).orElseThrow(EntityNotFoundException::new);
//        Role supplierRole = roleRepository.findById(3L).orElseThrow(EntityNotFoundException::new);
//        System.out.println();
//        Role adminRole = roleRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
//
//        User user = userRepository.findById(5L).orElseThrow(EntityNotFoundException::new);
//        Role newRole = roleRepository.findById(4L).get();
//        User admin = userRepository.findById(8L).orElseThrow(EntityNotFoundException::new);
//
//        userService.changeUserRole(admin, user, newRole);
//
//
//        User updatedClient = userRepository.findById(5L).orElseThrow(EntityNotFoundException::new);
//        System.out.println("New user type: " + updatedClient.getClass().getSimpleName());
//
//        supplierRole.addUser(client);
//
//        userService.register(admin);



    }
}