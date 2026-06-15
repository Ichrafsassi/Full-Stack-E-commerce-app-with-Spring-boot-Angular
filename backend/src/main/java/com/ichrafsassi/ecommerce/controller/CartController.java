package com.ichrafsassi.ecommerce.controller;

import com.ichrafsassi.ecommerce.dto.CartDto;
import com.ichrafsassi.ecommerce.dto.CartItemRequest;
import com.ichrafsassi.ecommerce.service.CartService;
import com.ichrafsassi.ecommerce.util.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CurrentUser currentUser;

    @GetMapping
    public CartDto get() {
        return cartService.getCart(currentUser.require());
    }

    @PostMapping("/items")
    public CartDto add(@Valid @RequestBody CartItemRequest request) {
        return cartService.addItem(currentUser.require(), request);
    }

    @PutMapping("/items/{productId}")
    public CartDto update(@PathVariable Long productId, @RequestParam int quantity) {
        return cartService.updateQuantity(currentUser.require(), productId, quantity);
    }

    @DeleteMapping("/items/{productId}")
    public CartDto remove(@PathVariable Long productId) {
        return cartService.removeItem(currentUser.require(), productId);
    }
}
