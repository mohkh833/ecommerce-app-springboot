package com.Ecommerce_backend.service.interfac;

import com.Ecommerce_backend.dto.GettingProductRequest;
import com.Ecommerce_backend.dto.LoginRequest;
import com.Ecommerce_backend.dto.Response;
import com.Ecommerce_backend.entity.Product;
import com.Ecommerce_backend.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface IProductService {

    Response addProduct(String userId, MultipartFile photo, String title, BigDecimal productPrice, String description,  List<String> categories);

    Response getAllProducts(GettingProductRequest gettingProductRequest);

    Response getProductById(String productId);

    Response getMyProducts(String username);

    Response deleteProduct(String productId);

    Response editProduct(String productId, MultipartFile photo, String title, BigDecimal productPrice, String description,  List<String> categories);

    Response searchProducts(String title, String description);

}
