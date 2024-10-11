package com.Ecommerce_backend.dto;

import com.Ecommerce_backend.entity.Order;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;
    private String message;

    private String token;
    private String role;
    private String expirationTime;
    private List<UserDTO> userList;
    private List<ProductDTO> productList;
    private UserDTO user;
    private ProductDTO product;
    private CartDTO cart;
    private OrderDTO order;
    private List<OrderDTO> orders;
    private CategoryDTO category;
    private List<CategoryDTO> categories;
}
