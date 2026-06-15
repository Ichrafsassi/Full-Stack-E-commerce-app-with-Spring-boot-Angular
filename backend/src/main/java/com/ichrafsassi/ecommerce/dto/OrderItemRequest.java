package com.ichrafsassi.ecommerce.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Shared line-item payload for order create and update requests.
 * Single record reused by both DTOs (DRY / SOLID).
 */
public record OrderItemRequest(@NotNull Long productId, @NotNull Integer quantity) {}
