package com.ichrafsassi.ecommerce.dto;

import com.ichrafsassi.ecommerce.domain.Product;
import com.ichrafsassi.ecommerce.util.PricingUtil;

import java.math.BigDecimal;

public record ProductDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        BigDecimal originalPrice,
        int discountPercent,
        BigDecimal savings,
        boolean onSale,
        Integer stock,
        String imageUrl,
        Long categoryId,
        String categoryName,
        boolean active
) {
    public static ProductDto from(Product p) {
        BigDecimal original = p.getOriginalPrice() != null ? p.getOriginalPrice() : p.getPrice();
        int discount = p.getDiscountPercent();
        boolean onSale = discount > 0 && original.compareTo(p.getPrice()) > 0;
        return new ProductDto(
                p.getId(), p.getName(), p.getDescription(), p.getPrice(), original, discount,
                PricingUtil.savings(original, p.getPrice()), onSale,
                p.getStock(), p.getImageUrl(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.isActive()
        );
    }
}
