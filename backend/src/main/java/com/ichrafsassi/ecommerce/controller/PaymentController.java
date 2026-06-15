package com.ichrafsassi.ecommerce.controller;

import com.ichrafsassi.ecommerce.dto.PaymentIntentResponse;
import com.ichrafsassi.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/intent")
    public PaymentIntentResponse intent(@RequestParam BigDecimal amount) {
        return paymentService.createIntent(amount);
    }
}
