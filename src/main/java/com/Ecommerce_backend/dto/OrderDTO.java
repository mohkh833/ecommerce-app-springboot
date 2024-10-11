package com.Ecommerce_backend.dto;

import com.Ecommerce_backend.entity.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {

     int userId;
     ShippingInfo shippingInfo;
     PaymentMethod paymentMethod;
     List<OrderItemDTO> orderItems;
     OrderStatus orderStatus = OrderStatus.PENDING;
     BigDecimal totalPrice;
     LocalDateTime orderDate = LocalDateTime.now();
     PaymentInfoRequest paymentInfoRequest;

}
