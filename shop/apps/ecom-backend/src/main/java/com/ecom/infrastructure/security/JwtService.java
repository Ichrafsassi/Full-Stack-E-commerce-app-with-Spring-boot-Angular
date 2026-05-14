package com.ecom.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Service
public class JwtService {
	private final SecretKey key;
	private final long expirationSeconds;

	public JwtService(
		@Value("${app.jwt.secret}") String secret,
		@Value("${app.jwt.expiration-seconds}") long expirationSeconds
	) {
		if (secret == null || secret.isBlank() || secret.length() < 32) {
			throw new IllegalArgumentException("app.jwt.secret must be at least 32 characters");
		}
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationSeconds = expirationSeconds;
	}

	public String generateToken(UserPrincipal principal, String role) {
		Instant now = Instant.now();
		Instant exp = now.plusSeconds(expirationSeconds);

		return Jwts
			.builder()
			.subject(principal.getUsername())
			.issuedAt(Date.from(now))
			.expiration(Date.from(exp))
			.claim("uid", principal.getId().toString())
			.claim("role", role)
			.signWith(key)
			.compact();
	}

	public JwtUser parse(String token) {
		Jws<Claims> jws = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
		Claims claims = jws.getPayload();

		String email = claims.getSubject();
		String uid = claims.get("uid", String.class);
		String role = claims.get("role", String.class);

		return new JwtUser(UUID.fromString(uid), email, role);
	}

	public record JwtUser(UUID userId, String email, String role) {
	}
}

