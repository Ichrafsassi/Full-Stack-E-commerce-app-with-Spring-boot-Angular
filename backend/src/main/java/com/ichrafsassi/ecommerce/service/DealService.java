package com.ichrafsassi.ecommerce.service;

import com.ichrafsassi.ecommerce.dto.DealsResponse;
import com.ichrafsassi.ecommerce.dto.ProductDto;
import com.ichrafsassi.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DealService {

    private final ProductRepository productRepository;

    public DealsResponse getActiveDeals() {
        var products = productRepository.findByActiveTrueAndDiscountPercentGreaterThanOrderByDiscountPercentDesc(0)
                .stream()
                .map(ProductDto::from)
                .toList();
        BigDecimal totalSavings = products.stream()
                .map(ProductDto::savings)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new DealsResponse(
                "NERD'S TECH Neo Deals",
                "Limited-time discounts — sale price = original × (1 − discount%).",
                products.size(),
                totalSavings,
                products
        );
    }
}
