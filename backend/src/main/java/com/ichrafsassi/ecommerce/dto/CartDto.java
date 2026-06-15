package com.ichrafsassi.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartDto(Long id, List<CartItemDto> items, BigDecimal total) {
    public record CartItemDto(Long id, Long productId, String productName, BigDecimal unitPrice,
                              Integer quantity, BigDecimal lineTotal, String imageUrl) {}
}
