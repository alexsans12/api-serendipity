package com.serendipity.ecommerce.dtomapper;

import com.serendipity.ecommerce.dto.PaymentIntentDTO;
import com.stripe.model.PaymentIntent;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PaymentIntentDTOMapper {
    public static PaymentIntentDTO toPaymentIntent(PaymentIntent paymentIntent) {
        PaymentIntentDTO dto = new PaymentIntentDTO();
        dto.setId(paymentIntent.getId());
        dto.setAmount(paymentIntent.getAmount());
        dto.setCaptureMethod(paymentIntent.getCaptureMethod());
        dto.setClientSecret(paymentIntent.getClientSecret());
        dto.setCreated(paymentIntent.getCreated());
        dto.setCurrency(paymentIntent.getCurrency());
        dto.setLivemode(paymentIntent.getLivemode());
        dto.setStatus(paymentIntent.getStatus());

        if (paymentIntent.getPaymentMethodTypes() != null) {
            dto.setPaymentMethodTypes(new ArrayList<>(paymentIntent.getPaymentMethodTypes()));
        }
        return dto;
    }
}
