package com.Ecommerce_backend.dto;

import com.Ecommerce_backend.entity.Order;
import com.Ecommerce_backend.entity.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private Long orderItemId;
    private Long orderId;
    private int quantity;
    private BigDecimal totalPrice;
    private BigDecimal unitPrice;

    public OrderItemDTO(Long productId, String productName, Long orderItemId, Long orderId, int quantity, BigDecimal totalPrice, BigDecimal unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.unitPrice = unitPrice;
    }
}
