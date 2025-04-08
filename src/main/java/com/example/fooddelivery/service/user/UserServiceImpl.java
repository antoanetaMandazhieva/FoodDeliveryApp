package com.example.fooddelivery.service.user;

import com.example.fooddelivery.config.order.OrderMapper;
import com.example.fooddelivery.config.user.UserMapper;
import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.dto.user.UserDto;
import com.example.fooddelivery.dto.user.UserProfileDto;
import com.example.fooddelivery.entity.Role;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.repository.OrderRepository;
import com.example.fooddelivery.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import com.example.fooddelivery.repository.UserRepository;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public UserProfileDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user not found"));

        return UserMapper.mapToUserProfileDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserProfileDto updateUser(Long id, UserProfileDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));


        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setGender(dto.getGender());

        userRepository.save(user);

        return UserMapper.mapToUserProfileDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }

        userRepository.deleteById(id);
    }

    @Override
    public void changeUserRole(Long adminId, Long userId, String newRole) throws AccessDeniedException {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Requester not found"));

        if (!admin.getRole().getName().equalsIgnoreCase("ADMIN")) {
            throw new AccessDeniedException("Only admins can change roles");
        }

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Role currentRole = targetUser.getRole();
        currentRole.removeUser(targetUser);

        Role updatedRole = roleRepository.findByName(newRole.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid role"));

        updatedRole.addUser(targetUser);


        userRepository.save(targetUser);
    }

    @Override
    public Long getUserIdFromUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"))
                .getId();
    }

    @Override
    public List<OrderResponseDto> getOrdersByClientUsername(String clientUsername) {
        User client = userRepository.findByUsername(clientUsername)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return orderRepository.findByClientId(client.getId())
                .stream()
                .map(OrderMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<OrderResponseDto> getOrdersBySupplierUsername(String supplierUsername) {
        User supplier = userRepository.findByUsername(supplierUsername)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        return orderRepository.findBySupplierId(supplier.getId())
                .stream()
                .map(OrderMapper::toResponseDto)
                .toList();
    }
}