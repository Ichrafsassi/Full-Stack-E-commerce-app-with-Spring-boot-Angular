package com.ichrafsassi.ecommerce.dto;

/**
 * Login/register response. Uses the shared {@link UserDto} record (no duplicate nested type).
 */
public record AuthResponse(String token, UserDto user) {}
