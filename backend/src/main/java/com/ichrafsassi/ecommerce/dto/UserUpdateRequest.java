package com.ichrafsassi.ecommerce.dto;

import com.ichrafsassi.ecommerce.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Email String email,
        @Size(min = 6) String password,
        String firstName,
        String lastName,
        Role role,
        Boolean enabled
) {}
