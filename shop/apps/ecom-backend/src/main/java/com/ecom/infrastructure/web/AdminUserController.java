package com.ecom.infrastructure.web;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.application.user.UserService;
import com.ecom.infrastructure.security.JwtService;
import com.ecom.infrastructure.web.dto.UpdateUserRoleRequest;
import com.ecom.infrastructure.web.dto.UserResponse;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
	private final UserService userService;

	public AdminUserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<UserResponse>> listUsers() {
		return ResponseEntity.ok(userService.listUsers());
	}

	@PostMapping
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
		return ResponseEntity.ok(
			userService.createUser(request.email(), request.password(), request.firstName(), request.lastName(), request.role())
		);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> updateUserProfile(
		@PathVariable UUID id,
		@Valid @RequestBody UpdateUserRequest request
	) {
		return ResponseEntity.ok(userService.updateUserProfile(id, request.firstName(), request.lastName()));
	}

	@PutMapping("/{id}/role")
	public ResponseEntity<UserResponse> updateRole(
		@PathVariable UUID id,
		Authentication authentication,
		@Valid @RequestBody UpdateUserRoleRequest request
	) {
		JwtService.JwtUser jwtUser = (JwtService.JwtUser) authentication.getPrincipal();
		return ResponseEntity.ok(userService.updateRole(jwtUser.userId(), id, request.role()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable UUID id, Authentication authentication) {
		JwtService.JwtUser jwtUser = (JwtService.JwtUser) authentication.getPrincipal();
		userService.deleteUser(jwtUser.userId(), id);
		return ResponseEntity.noContent().build();
	}

	public record CreateUserRequest(
		@Email @NotBlank String email,
		@NotBlank String password,
		@NotBlank String firstName,
		@NotBlank String lastName,
		@NotBlank String role
	) {
	}

	public record UpdateUserRequest(@NotBlank String firstName, @NotBlank String lastName) {
	}
}
