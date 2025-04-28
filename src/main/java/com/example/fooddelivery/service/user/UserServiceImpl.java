package com.example.fooddelivery.service.user;

import com.example.fooddelivery.dto.order.OrderResponseDto;
import com.example.fooddelivery.dto.user.UserDto;
import com.example.fooddelivery.dto.user.UserProfileDto;
import com.example.fooddelivery.entity.address.Address;
import com.example.fooddelivery.entity.role.Role;
import com.example.fooddelivery.entity.user.User;
import com.example.fooddelivery.exception.role.InvalidRoleException;
import com.example.fooddelivery.exception.user.FieldAlreadyTakenException;
import com.example.fooddelivery.mapper.address.AddressMapper;
import com.example.fooddelivery.mapper.order.OrderMapper;
import com.example.fooddelivery.mapper.user.UserMapper;
import com.example.fooddelivery.repository.OrderRepository;
import com.example.fooddelivery.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.example.fooddelivery.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.fooddelivery.util.SystemErrors.Role.INVALID_ROLE;
import static com.example.fooddelivery.util.SystemErrors.User.*;

@Service
public class UserServiceImpl implements UserService {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String EMPLOYEE_ROLE = "EMPLOYEE";
    private static final String SUPPLIER_ROLE = "SUPPLIER";

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
        User user = getUser(id);

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
        User user = getUser(id);
        validateUsernameEmailAndPhoneNumber(user, dto);

        updateBasicUserInfo(user, dto);
        updateUserAddresses(user, dto);


        return userMapper.mapToUserProfileDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUser(id);

        user.detachAllRelations();

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void changeUserRole(Long adminId, Long userId, String newRole) {
        User admin = getUser(adminId);
        validateIsAdmin(admin);

        User targetUser = getUser(userId);

        Role currentRole = targetUser.getRole();
        currentRole.removeUser(targetUser);


        Role updatedRole = getRole(newRole);

        updatedRole.addUser(targetUser);


        userRepository.save(targetUser);
    }

    @Override
    public Long getUserIdFromUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND))
                .getId();
    }

    @Override
    @Transactional
    public List<OrderResponseDto> getOrdersByClientUsername(String clientUsername) {
        User client = getUserByUsername(clientUsername);

        return orderRepository.findByClientId(client.getId())
                .stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public List<OrderResponseDto> getOrdersBySupplierUsername(String supplierUsername, Long workerId) {
        User worker = getUser(workerId);
        String workerRole = worker.getRole().getName();

        validateIsAdminOrEmployee(workerRole);

        User supplier = getUserByUsername(supplierUsername);
        validateIsSupplier(supplier, ONLY_SUPPLIER_ORDERS_CAN_BE_CHECKED);

        return orderRepository.findBySupplierId(supplier.getId())
                .stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Override
    public Long getSupplierIdByUsername(String username) {
        User supplier = getUserByUsername(username);

        validateIsSupplier(supplier, SUPPLIER_NOT_FOUND);

        return supplier.getId();
    }



    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }

    private void validateUsernameEmailAndPhoneNumber(User user, UserProfileDto dto) {
        if (!dto.getUsername().equals(user.getUsername()) && userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new FieldAlreadyTakenException(USERNAME_ALREADY_TAKEN);
        }

        if (!dto.getEmail().equals(user.getEmail()) && userRepository.findByUsername(dto.getEmail()).isPresent()) {
            throw new FieldAlreadyTakenException(EMAIL_ALREADY_TAKEN);
        }

        if (!dto.getPhoneNumber().equals(user.getPhoneNumber()) &&
                userRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new FieldAlreadyTakenException(PHONE_NUMBER_ALREADY_TAKEN);
        }
    }

    private void updateBasicUserInfo(User user, UserProfileDto dto) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setGender(dto.getGender());
    }

    private void updateUserAddresses(User user, UserProfileDto dto) {
        Set<Address> addressesFromDto = dto.getAddresses().stream()
                .map(addressMapper::mapToAddress)
                .collect(Collectors.toSet());


        for (Address addressFromDto : addressesFromDto) {
            if (!user.getAddresses().contains(addressFromDto)) {
                user.addAddress(addressFromDto);
            }
        }
    }

    private void validateIsAdmin(User admin) {
        if (!ADMIN_ROLE.equals(admin.getRole().getName())) {
            throw new InvalidRoleException(ONLY_ADMIN_CHANGE_ROLE);
        }
    }

    private Role getRole(String newRole) {
        return roleRepository.findByName(newRole.toUpperCase())
                .orElseThrow(() -> new InvalidRoleException(INVALID_ROLE));
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }

    private void validateIsAdminOrEmployee(String workerRole) {
        if (!ADMIN_ROLE.equals(workerRole) && !EMPLOYEE_ROLE.equals(workerRole)) {
            throw new InvalidRoleException(NO_PERMISSION_TO_SEE_SUPPLIER_ORDERS);
        }
    }

    private void validateIsSupplier(User supplier, String message) {
        if (!SUPPLIER_ROLE.equals(supplier.getRole().getName())) {
            throw new InvalidRoleException(message);
        }
    }
}