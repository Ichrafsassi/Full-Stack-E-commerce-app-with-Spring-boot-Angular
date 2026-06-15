package com.ichrafsassi.ecommerce.dto;

import com.ichrafsassi.ecommerce.domain.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SiteContentRequest(
        @NotBlank String contentKey,
        @NotBlank String title,
        String body,
        @NotNull ContentType type,
        boolean published
) {}
