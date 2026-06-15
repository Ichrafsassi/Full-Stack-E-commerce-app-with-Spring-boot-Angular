package com.ichrafsassi.ecommerce.dto;

import com.ichrafsassi.ecommerce.domain.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrderUpdateRequest(
    @NotBlank String shippingAddress,
    @NotNull OrderStatus status,
    @NotEmpty List<OrderItemRequest> items
) {}
