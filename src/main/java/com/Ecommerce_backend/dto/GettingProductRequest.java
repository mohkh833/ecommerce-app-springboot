package com.Ecommerce_backend.dto;

import com.Ecommerce_backend.Filter.ProductFilter;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GettingProductRequest {
    int pageNumber;
    int pageSize;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private List<String> categories;

}



