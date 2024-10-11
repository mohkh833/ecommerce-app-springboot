package com.Ecommerce_backend.Filter;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
public class ProductFilter {
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private String title;
    private String description;
    private List<String> categories;

    // Constructors, getters, and setters...

}