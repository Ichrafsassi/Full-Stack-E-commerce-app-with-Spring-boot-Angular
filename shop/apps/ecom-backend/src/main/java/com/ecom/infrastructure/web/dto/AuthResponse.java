package com.ecom.infrastructure.web.dto;

public record AuthResponse(String token, UserResponse user) {
}

