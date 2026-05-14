package com.ecom.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.domain.user.AppUser;
import com.ecom.domain.user.UserRole;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
	Optional<AppUser> findByEmail(String email);

	boolean existsByEmail(String email);

	long countByRole(UserRole role);
}
