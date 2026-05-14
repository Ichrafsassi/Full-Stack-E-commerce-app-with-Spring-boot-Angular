package com.ecom.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.application.user.UserService;
import com.ecom.infrastructure.security.JwtService;
import com.ecom.infrastructure.web.dto.UserResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	public ResponseEntity<UserResponse> me(Authentication authentication) {
		JwtService.JwtUser jwtUser = (JwtService.JwtUser) authentication.getPrincipal();
		return ResponseEntity.ok(userService.me(jwtUser.userId()));
	}
}

