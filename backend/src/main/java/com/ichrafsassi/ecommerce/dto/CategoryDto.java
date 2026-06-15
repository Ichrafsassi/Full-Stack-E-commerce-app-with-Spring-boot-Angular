package com.ichrafsassi.ecommerce.dto;

import com.ichrafsassi.ecommerce.domain.Category;

public record CategoryDto(Long id, String name, String description, String imageUrl) {
    public static CategoryDto from(Category c) {
        return new CategoryDto(c.getId(), c.getName(), c.getDescription(), c.getImageUrl());
    }
}
