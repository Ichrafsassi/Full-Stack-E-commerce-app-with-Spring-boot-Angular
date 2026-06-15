package com.ichrafsassi.ecommerce.controller;

import com.ichrafsassi.ecommerce.dto.CheckoutRequest;
import com.ichrafsassi.ecommerce.dto.OrderDto;
import com.ichrafsassi.ecommerce.service.OrderService;
import com.ichrafsassi.ecommerce.util.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CurrentUser currentUser;

    @GetMapping
    public List<OrderDto> myOrders() {
        return orderService.myOrders(currentUser.require());
    }

    @PostMapping("/checkout")
    public OrderDto checkout(@Valid @RequestBody CheckoutRequest request) {
        return orderService.checkout(currentUser.require(), request);
    }

    @GetMapping("/{id}")
    public OrderDto get(@PathVariable Long id) {
        return orderService.findByIdForUser(id, currentUser.require());
    }
}
