package com.ichrafsassi.ecommerce.service;

import com.ichrafsassi.ecommerce.dto.PaymentIntentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentService {

    @Value("${app.stripe.enabled:false}")
    private boolean stripeEnabled;

    public PaymentIntentResponse createIntent(BigDecimal amount) {
        return new PaymentIntentResponse(
                "pi_sim_" + UUID.randomUUID(),
                amount,
                !stripeEnabled
        );
    }

    public String createSimulatedIntent(BigDecimal amount) {
        return createIntent(amount).clientSecret();
    }
}
