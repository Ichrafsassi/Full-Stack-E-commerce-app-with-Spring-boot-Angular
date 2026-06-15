package com.ichrafsassi.ecommerce.dto;

import com.ichrafsassi.ecommerce.domain.Order;
import com.ichrafsassi.ecommerce.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDto(Long id, OrderStatus status, BigDecimal totalAmount, String shippingAddress,
                       Instant createdAt, List<OrderItemDto> items, String customerEmail) {
    public record OrderItemDto(Long productId, String productName, BigDecimal unitPrice, Integer quantity, BigDecimal lineTotal) {}

    public static OrderDto from(Order order) {
        var items = order.getItems().stream()
                .map(i -> new OrderItemDto(i.getProductId(), i.getProductName(), i.getUnitPrice(), i.getQuantity(), i.getLineTotal()))
                .toList();
        return new OrderDto(
                order.getId(), order.getStatus(), order.getTotalAmount(), order.getShippingAddress(),
                order.getCreatedAt(), items, order.getUser().getEmail()
        );
    }
}
