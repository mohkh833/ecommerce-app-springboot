package com.Ecommerce_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDTO {
    @Min(value = 0, message = "rate should not be less than 0")
    @Max(value = 5, message = "rate should not be greater than 5")
    private int rate;

    private String reviewDescription;
}
