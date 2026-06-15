package com.ichrafsassi.ecommerce.dto;

import com.ichrafsassi.ecommerce.domain.Role;
import com.ichrafsassi.ecommerce.domain.User;

public record UserDto(Long id, String email, String firstName, String lastName, Role role, boolean enabled) {
    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole(), user.isEnabled());
    }
}
