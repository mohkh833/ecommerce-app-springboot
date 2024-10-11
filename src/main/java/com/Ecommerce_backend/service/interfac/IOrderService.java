package com.Ecommerce_backend.service.interfac;

import com.Ecommerce_backend.dto.PaymentInfoRequest;
import com.Ecommerce_backend.dto.Response;
import com.Ecommerce_backend.entity.PaymentMethod;
import com.Ecommerce_backend.entity.ShippingInfo;
import com.Ecommerce_backend.entity.User;

public interface IOrderService {

    Response createOrder(int userId, ShippingInfo shippingInfo, PaymentMethod paymentMethod, PaymentInfoRequest paymentInfoRequest);

    Response deleteOrder(int orderId);

    Response viewOrdersHistory(int userId);
}
