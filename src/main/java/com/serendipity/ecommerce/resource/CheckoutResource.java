package com.serendipity.ecommerce.resource;

import com.serendipity.ecommerce.domain.HttpResponse;
import com.serendipity.ecommerce.domain.PaymentInfo;
import com.serendipity.ecommerce.dtomapper.PaymentIntentDTOMapper;
import com.serendipity.ecommerce.exception.ApiException;
import com.serendipity.ecommerce.service.CheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.serendipity.ecommerce.dtomapper.PaymentIntentDTOMapper.toPaymentIntent;
import static java.time.LocalTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutResource {
    private final CheckoutService checkoutService;

    @PostMapping("/payment-intent")
    public ResponseEntity<HttpResponse> createPaymentIntent(@RequestBody PaymentInfo paymentInfo) throws StripeException {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(now().toString())
                        .data(of("paymentIntent", toPaymentIntent(checkoutService.createPaymentIntent(paymentInfo))))
                        .message("Payment intent created successfully")
                        .httpStatus(OK)
                        .httpStatusCode(OK.value())
                        .build()
        );
    }
}
