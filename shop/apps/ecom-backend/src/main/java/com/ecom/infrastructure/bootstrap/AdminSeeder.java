package com.ecom.infrastructure.bootstrap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.domain.user.AppUser;
import com.ecom.domain.user.UserRole;
import com.ecom.infrastructure.persistence.AppUserRepository;

@Component
public class AdminSeeder implements ApplicationRunner {
	private final AppUserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final String adminEmail;
	private final String adminPassword;

	public AdminSeeder(
		AppUserRepository userRepository,
		PasswordEncoder passwordEncoder,
		@Value("${APP_ADMIN_EMAIL:admin@local}") String adminEmail,
		@Value("${APP_ADMIN_PASSWORD:admin12345}") String adminPassword
	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.adminEmail = adminEmail.toLowerCase();
		this.adminPassword = adminPassword;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		if (userRepository.existsByEmail(adminEmail)) {
			return;
		}

		AppUser admin = new AppUser(
			adminEmail,
			passwordEncoder.encode(adminPassword),
			"Admin",
			"User",
			UserRole.ADMIN
		);
		userRepository.save(admin);
	}
}

