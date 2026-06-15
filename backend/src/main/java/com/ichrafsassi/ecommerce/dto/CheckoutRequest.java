package com.ichrafsassi.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record CheckoutRequest(@NotBlank String shippingAddress, boolean useStripe) {}
