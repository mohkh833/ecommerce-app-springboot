package com.Ecommerce_backend.service.interfac;

import com.Ecommerce_backend.dto.CartDTO;
import com.Ecommerce_backend.dto.Response;
import com.Ecommerce_backend.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ICartService {


    Response getMyCarts(String username);

    Response addItemToCart(int quantity, Product product, int userId);

    Response updateItemQuantity(int quantity, Product product, int userId );

    Response removeItemFromCart(int userId, Product product);

    Response clearCart(int userId);

}
