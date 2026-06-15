package com.ichrafsassi.ecommerce.controller;

import com.ichrafsassi.ecommerce.dto.DealsResponse;
import com.ichrafsassi.ecommerce.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @GetMapping
    public DealsResponse list() {
        return dealService.getActiveDeals();
    }
}
