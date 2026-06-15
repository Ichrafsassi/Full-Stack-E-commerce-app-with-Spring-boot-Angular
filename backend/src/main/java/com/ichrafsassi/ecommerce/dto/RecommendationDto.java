package com.ichrafsassi.ecommerce.dto;

import java.util.List;

public record RecommendationDto(String message, List<ProductDto> products) {}
