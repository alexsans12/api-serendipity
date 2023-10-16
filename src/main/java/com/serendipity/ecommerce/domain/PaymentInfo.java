package com.serendipity.ecommerce.domain;

import lombok.Data;

@Data
public class PaymentInfo {
    private int amount;
    private String currency;
}
