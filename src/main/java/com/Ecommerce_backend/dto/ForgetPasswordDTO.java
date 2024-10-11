package com.Ecommerce_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForgetPasswordDTO {
    String confirmationToken;
    String password;
    String email;
}
