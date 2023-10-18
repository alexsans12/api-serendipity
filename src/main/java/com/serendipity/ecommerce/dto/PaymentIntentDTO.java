package com.serendipity.ecommerce.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentIntentDTO {
    private String id;
    private Long amount;
    private String captureMethod;
    private String clientSecret;
    private Long created;
    private String currency;
    private Boolean livemode;
    private List<String> paymentMethodTypes;
    private String status;
}
