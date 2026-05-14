package com.ecom.application.user;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.domain.user.AppUser;
import com.ecom.domain.user.UserRole;
import com.ecom.infrastructure.persistence.AppUserRepository;
import com.ecom.infrastructure.web.dto.UserResponse;

@Service
public class UserService {
	private final AppUserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public UserResponse me(UUID userId) {
		return userRepository
			.findById(userId)
			.map(user -> new UserResponse(
				user.getId(),
				user.getEmail(),
				user.getFirstName(),
				user.getLastName(),
				user.getRole().name()
			))
			.orElseThrow(() -> new IllegalStateException("User not found"));
	}

	@Transactional(readOnly = true)
	public List<UserResponse> listUsers() {
		return userRepository
			.findAll()
			.stream()
			.sorted((left, right) -> right.getCreatedAt().compareTo(left.getCreatedAt()))
			.map(user -> new UserResponse(
				user.getId(),
				user.getEmail(),
				user.getFirstName(),
				user.getLastName(),
				user.getRole().name()
			))
			.toList();
	}

	@Transactional
	public UserResponse updateRole(UUID actorUserId, UUID userId, String role) {
		UserRole newRole;
		try {
			newRole = UserRole.valueOf(role.toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Unsupported role: " + role);
		}

		if (actorUserId.equals(userId) && newRole != UserRole.ADMIN) {
			throw new IllegalArgumentException("You cannot remove your own admin access");
		}

		return userRepository
			.findById(userId)
			.map(user -> {
				if (user.getRole() == UserRole.ADMIN && newRole != UserRole.ADMIN) {
					long adminCount = userRepository.countByRole(UserRole.ADMIN);
					if (adminCount <= 1) {
						throw new IllegalArgumentException("At least one admin user must remain");
					}
				}

				user.setRole(newRole);
				return new UserResponse(
					user.getId(),
					user.getEmail(),
					user.getFirstName(),
					user.getLastName(),
					user.getRole().name()
				);
			})
			.orElseThrow(() -> new IllegalStateException("User not found"));
	}

	@Transactional
	public UserResponse createUser(String email, String password, String firstName, String lastName, String role) {
		String normalizedEmail = email == null ? null : email.trim().toLowerCase();
		if (normalizedEmail == null || normalizedEmail.isBlank()) {
			throw new IllegalArgumentException("Email is required");
		}
		if (password == null || password.isBlank() || password.length() < 8) {
			throw new IllegalArgumentException("Password must be at least 8 characters");
		}
		if (firstName == null || firstName.isBlank()) {
			throw new IllegalArgumentException("First name is required");
		}
		if (lastName == null || lastName.isBlank()) {
			throw new IllegalArgumentException("Last name is required");
		}
		if (userRepository.existsByEmail(normalizedEmail)) {
			throw new IllegalArgumentException("Email already in use");
		}

		UserRole userRole;
		try {
			userRole = UserRole.valueOf(role.toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Unsupported role: " + role);
		}

		AppUser user = new AppUser(
			normalizedEmail,
			passwordEncoder.encode(password),
			firstName.trim(),
			lastName.trim(),
			userRole
		);
		userRepository.save(user);
		return new UserResponse(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole().name());
	}

	@Transactional
	public UserResponse updateUserProfile(UUID userId, String firstName, String lastName) {
		if (firstName == null || firstName.isBlank()) {
			throw new IllegalArgumentException("First name is required");
		}
		if (lastName == null || lastName.isBlank()) {
			throw new IllegalArgumentException("Last name is required");
		}

		return userRepository
			.findById(userId)
			.map(user -> {
				user.updateProfile(firstName.trim(), lastName.trim());
				return new UserResponse(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole().name());
			})
			.orElseThrow(() -> new IllegalStateException("User not found"));
	}

	@Transactional
	public void deleteUser(UUID actorUserId, UUID userId) {
		if (actorUserId.equals(userId)) {
			throw new IllegalArgumentException("You cannot delete your own account");
		}

		AppUser user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));
		if (user.getRole() == UserRole.ADMIN) {
			long adminCount = userRepository.countByRole(UserRole.ADMIN);
			if (adminCount <= 1) {
				throw new IllegalArgumentException("At least one admin user must remain");
			}
		}
		userRepository.deleteById(userId);
	}
}
