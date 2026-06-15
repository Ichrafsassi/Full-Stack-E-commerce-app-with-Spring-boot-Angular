package com.ichrafsassi.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;

public record DealsResponse(
        String title,
        String message,
        int dealCount,
        BigDecimal totalPotentialSavings,
        List<ProductDto> products
) {}
