package com.ecom.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecom.infrastructure.persistence.AppUserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {
	private final AppUserRepository userRepository;

	public AppUserDetailsService(AppUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository
			.findByEmail(username)
			.map(UserPrincipal::new)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
}

