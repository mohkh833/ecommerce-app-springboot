package com.Ecommerce_backend.service.interfac;

import com.Ecommerce_backend.dto.PaymentInfoRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface IPaymentService {

    PaymentIntent createPaymentIntent(PaymentInfoRequest paymentInfoRequest)  throws StripeException;
}
