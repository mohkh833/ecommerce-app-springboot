package com.Ecommerce_backend.dto;

import com.Ecommerce_backend.entity.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {
    private Long id;
    private String title;
    private String desc;
    private String image;
    private BigDecimal productPrice;
    private List<String> categories;
    private boolean inStock;
    private UserDTO user;

}
