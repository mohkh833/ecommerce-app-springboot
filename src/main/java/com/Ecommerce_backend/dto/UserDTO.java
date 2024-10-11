package com.Ecommerce_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private String name;
    private String phoneNumber;
    private String role;
    private List<ProductDTO> products = new ArrayList<>();
}
