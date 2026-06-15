package com.ichrafsassi.ecommerce.service;

import com.ichrafsassi.ecommerce.domain.User;
import com.ichrafsassi.ecommerce.dto.ProductDto;
import com.ichrafsassi.ecommerce.dto.RecommendationDto;
import com.ichrafsassi.ecommerce.repository.OrderRepository;
import com.ichrafsassi.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Value("${app.ai.enabled:true}")
    private boolean aiEnabled;

    public RecommendationDto recommend(User user) {
        if (!aiEnabled) {
            return new RecommendationDto("AI recommendations are disabled", List.of());
        }
        Set<Long> purchasedCategoryIds = orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .flatMap(o -> o.getItems().stream())
                .map(i -> productRepository.findById(i.getProductId()).map(p -> p.getCategory() != null ? p.getCategory().getId() : null))
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());

        var products = productRepository.findByActiveTrue().stream()
                .filter(p -> p.getCategory() != null && purchasedCategoryIds.contains(p.getCategory().getId()))
                .limit(6)
                .map(ProductDto::from)
                .toList();

        if (products.isEmpty()) {
            products = productRepository.findTop6ByActiveTrueOrderByIdDesc().stream()
                    .map(ProductDto::from).toList();
        }

        String message = products.isEmpty()
                ? "Browse our catalog to get personalized suggestions."
                : "Based on your order history, you may also like:";
        return new RecommendationDto(message, products);
    }

    public RecommendationDto guestRecommendations() {
        var products = productRepository.findTop6ByActiveTrueOrderByIdDesc().stream()
                .map(ProductDto::from).toList();
        return new RecommendationDto("Popular picks for you", products);
    }
}
