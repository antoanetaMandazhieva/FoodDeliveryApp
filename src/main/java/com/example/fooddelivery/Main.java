package com.example.fooddelivery;

import com.example.fooddelivery.entity.Client;
import com.example.fooddelivery.entity.Role;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.enums.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.fooddelivery.repository.RoleRepository;
import com.example.fooddelivery.service.user_service.UserService;

import java.util.Optional;

@Component
public class Main implements CommandLineRunner {

    private UserService userService;
    private RoleRepository roleRepository;

    public Main(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        User client = new Client("kgasfjkf.123@abv.bg", "1234567AA", "ahil123", "Ahil", "Ahilov",
                Gender.MALE, 17, 10, 2002, "0888101010");

        Optional<Role> clientRole = roleRepository.findById(2L);

        if (clientRole.isPresent()) {
            client.setRole(clientRole.get());

            userService.register(client);
        }



    }
}