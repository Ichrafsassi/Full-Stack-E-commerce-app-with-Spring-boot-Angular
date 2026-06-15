package com.ichrafsassi.ecommerce.service;

import com.ichrafsassi.ecommerce.domain.Cart;
import com.ichrafsassi.ecommerce.domain.Role;
import com.ichrafsassi.ecommerce.domain.User;
import com.ichrafsassi.ecommerce.dto.UserCreateRequest;
import com.ichrafsassi.ecommerce.dto.UserDto;
import com.ichrafsassi.ecommerce.dto.UserUpdateRequest;
import com.ichrafsassi.ecommerce.exception.BadRequestException;
import com.ichrafsassi.ecommerce.exception.ResourceNotFoundException;
import com.ichrafsassi.ecommerce.repository.CartRepository;
import com.ichrafsassi.ecommerce.repository.OrderRepository;
import com.ichrafsassi.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserDto::from).toList();
    }

    public UserDto findById(Long id) {
        return UserDto.from(getEntity(id));
    }

    @Transactional
    public UserDto create(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already exists");
        }
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(request.role())
                .enabled(request.enabled())
                .build();
        user = userRepository.save(user);
        cartRepository.save(Cart.builder().user(user).build());
        return UserDto.from(user);
    }

    @Transactional
    public UserDto update(Long id, UserUpdateRequest request) {
        User user = getEntity(id);
        if (request.email() != null && !request.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.email())) {
                throw new BadRequestException("Email already exists");
            }
            user.setEmail(request.email());
        }
        if (request.password() != null) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        if (request.firstName() != null) user.setFirstName(request.firstName());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.role() != null) user.setRole(request.role());
        if (request.enabled() != null) user.setEnabled(request.enabled());
        return UserDto.from(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        User user = getEntity(id);
        if (user.getRole() == Role.ADMIN) {
            throw new BadRequestException("Cannot delete admin accounts");
        }
        orderRepository.findByUserOrderByCreatedAtDesc(user).forEach(orderRepository::delete);
        cartRepository.findByUser(user).ifPresent(cartRepository::delete);
        userRepository.delete(user);
    }

    private User getEntity(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
