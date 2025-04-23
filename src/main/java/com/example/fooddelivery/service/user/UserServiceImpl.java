package com.example.fooddelivery.service.user;

import com.example.fooddelivery.config.address.AddressMapper;
import com.example.fooddelivery.config.order.OrderMapper;
import com.example.fooddelivery.config.user.UserMapper;
import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.dto.user.UserDto;
import com.example.fooddelivery.dto.user.UserProfileDto;
import com.example.fooddelivery.entity.address.Address;
import com.example.fooddelivery.entity.role.Role;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.repository.OrderRepository;
import com.example.fooddelivery.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.example.fooddelivery.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;
    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final AddressMapper addressMapper;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           OrderRepository orderRepository,
                           UserMapper userMapper,
                           OrderMapper orderMapper, AddressMapper addressMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.orderRepository = orderRepository;
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;
        this.addressMapper = addressMapper;
    }

    @Override
    @Transactional
    public UserProfileDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userMapper.mapToUserProfileDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserProfileDto updateUser(Long id, UserProfileDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        validateUsernameEmailAndPhoneNumber(user, dto);

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setGender(dto.getGender());

        Set<Address> addressesFromDto = dto.getAddresses().stream()
                .map(addressMapper::mapToAddress)
                .collect(Collectors.toSet());


        for (Address addressFromDto : addressesFromDto) {
            if (!user.getAddresses().contains(addressFromDto)) {
                user.addAddress(addressFromDto);
            }
        }

        userRepository.save(user);

        return userMapper.mapToUserProfileDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.detachAllRelations();

        userRepository.delete(user);
    }

    @Override
    @Transactional
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
    @Transactional
    public List<OrderResponseDto> getOrdersByClientUsername(String clientUsername) {
        User client = userRepository.findByUsername(clientUsername)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return orderRepository.findByClientId(client.getId())
                .stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public List<OrderResponseDto> getOrdersBySupplierUsername(String supplierUsername, Long workerId) {
        User worker = userRepository.findById(workerId)
                .orElseThrow(() -> new EntityNotFoundException("Worker not found"));

        String workerRole = worker.getRole().getName();

        if (!workerRole.equals("ADMIN") && !workerRole.equals("EMPLOYEE")) {
            throw new IllegalStateException("You don't have permission to see orders of supplier");
        }


        User supplier = userRepository.findByUsername(supplierUsername)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));


        return orderRepository.findBySupplierId(supplier.getId())
                .stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    public Long getSupplierIdByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!"SUPPLIER".equals(user.getRole().getName())) {
            throw new IllegalArgumentException("User don't have SUPPLIER role");
        }

        return user.getId();
    }


    private void validateUsernameEmailAndPhoneNumber(User user, UserProfileDto dto) {
        if (!dto.getUsername().equals(user.getUsername()) && userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        if (!dto.getEmail().equals(user.getEmail()) && userRepository.findByUsername(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already taken");
        }

        if (!dto.getPhoneNumber().equals(user.getPhoneNumber()) &&
                userRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Phone number already taken");
        }
    }
}