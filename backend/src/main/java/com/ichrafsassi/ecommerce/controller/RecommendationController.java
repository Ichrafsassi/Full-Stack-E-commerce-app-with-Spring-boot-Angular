package com.ichrafsassi.ecommerce.controller;

import com.ichrafsassi.ecommerce.dto.RecommendationDto;
import com.ichrafsassi.ecommerce.service.RecommendationService;
import com.ichrafsassi.ecommerce.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final CurrentUser currentUser;

    @GetMapping
    public RecommendationDto recommend(Authentication auth) {
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return recommendationService.recommend(currentUser.require());
        }
        return recommendationService.guestRecommendations();
    }
}
