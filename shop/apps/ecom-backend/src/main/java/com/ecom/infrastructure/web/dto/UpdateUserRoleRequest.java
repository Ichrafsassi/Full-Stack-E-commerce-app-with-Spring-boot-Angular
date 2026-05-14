package com.ecom.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRoleRequest(@NotBlank String role) {
}

