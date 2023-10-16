package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.PaymentInfo;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {
    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
