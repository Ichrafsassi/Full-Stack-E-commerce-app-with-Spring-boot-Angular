package com.ecom.application.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.domain.user.AppUser;
import com.ecom.domain.user.UserRole;
import com.ecom.infrastructure.persistence.AppUserRepository;
import com.ecom.infrastructure.security.JwtService;
import com.ecom.infrastructure.security.UserPrincipal;
import com.ecom.infrastructure.web.dto.AuthResponse;
import com.ecom.infrastructure.web.dto.LoginRequest;
import com.ecom.infrastructure.web.dto.RegisterRequest;
import com.ecom.infrastructure.web.dto.UserResponse;

@Service
public class AuthService {
	private final AppUserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthService(
		AppUserRepository userRepository,
		PasswordEncoder passwordEncoder,
		AuthenticationManager authenticationManager,
		JwtService jwtService
	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@Transactional
	public AuthResponse register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new IllegalArgumentException("Email already in use");
		}

		AppUser user = new AppUser(
			request.email().toLowerCase(),
			passwordEncoder.encode(request.password()),
			request.firstName(),
			request.lastName(),
			UserRole.USER
		);
		userRepository.save(user);

		UserPrincipal principal = new UserPrincipal(user);
		String token = jwtService.generateToken(principal, user.getRole().name());

		return new AuthResponse(token, toUserResponse(user));
	}

	@Transactional(readOnly = true)
	public AuthResponse login(LoginRequest request) {
		var auth = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password())
		);

		Object principalObj = auth.getPrincipal();
		if (!(principalObj instanceof UserPrincipal principal)) {
			throw new IllegalStateException("Unexpected principal type");
		}

		AppUser user = userRepository
			.findByEmail(principal.getUsername())
			.orElseThrow(() -> new IllegalStateException("User not found"));

		String token = jwtService.generateToken(principal, user.getRole().name());
		return new AuthResponse(token, toUserResponse(user));
	}

	private UserResponse toUserResponse(AppUser user) {
		return new UserResponse(
			user.getId(),
			user.getEmail(),
			user.getFirstName(),
			user.getLastName(),
			user.getRole().name()
		);
	}
}

