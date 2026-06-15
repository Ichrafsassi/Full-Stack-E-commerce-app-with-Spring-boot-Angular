package com.ichrafsassi.ecommerce.util;

import com.ichrafsassi.ecommerce.domain.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PricingUtil {

    private PricingUtil() {}

    public static BigDecimal salePrice(BigDecimal originalPrice, int discountPercent) {
        if (originalPrice == null || discountPercent <= 0) {
            return originalPrice;
        }
        BigDecimal multiplier = BigDecimal.valueOf(100 - discountPercent)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        return originalPrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal savings(BigDecimal originalPrice, BigDecimal salePrice) {
        if (originalPrice == null || salePrice == null) {
            return BigDecimal.ZERO;
        }
        return originalPrice.subtract(salePrice).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }

    public static void applyTo(Product product, BigDecimal originalPrice, int discountPercent) {
        int pct = Math.max(0, Math.min(90, discountPercent));
        product.setOriginalPrice(originalPrice);
        product.setDiscountPercent(pct);
        product.setPrice(pct > 0 ? salePrice(originalPrice, pct) : originalPrice);
    }

    public static void normalize(Product product) {
        if (product.getOriginalPrice() == null) {
            product.setOriginalPrice(product.getPrice());
        }
        if (product.getDiscountPercent() > 0) {
            product.setPrice(salePrice(product.getOriginalPrice(), product.getDiscountPercent()));
        } else {
            product.setPrice(product.getOriginalPrice());
            product.setDiscountPercent(0);
        }
    }
}
