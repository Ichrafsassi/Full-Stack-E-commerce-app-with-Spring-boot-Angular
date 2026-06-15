package com.ichrafsassi.ecommerce.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        BigDecimal originalPrice,
        @Min(0) @Max(90) Integer discountPercent,
        @NotNull @Min(0) Integer stock,
        String imageUrl,
        Long categoryId,
        boolean active
) {}
