package com.ecommerce.security.dtos;

import lombok.Data;

@Data
public class UserValidationRequestDto {
    private String correlationId;
    private String emailOrPhone;
    private String password;
}
