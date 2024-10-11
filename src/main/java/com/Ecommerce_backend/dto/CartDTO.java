package com.Ecommerce_backend.dto;

import com.Ecommerce_backend.entity.CartItem;
import com.Ecommerce_backend.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDTO {
    int quantity;
    int shippingCost;
    Product product;
    int userId;
    int totalPrice;
    List<CartItemDTO> cartItems;
    String CartStatus;
}
