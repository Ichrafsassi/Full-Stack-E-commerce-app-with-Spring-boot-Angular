package com.ichrafsassi.ecommerce.dto;

import java.math.BigDecimal;

public record PaymentIntentResponse(String clientSecret, BigDecimal amount, boolean simulated) {}
