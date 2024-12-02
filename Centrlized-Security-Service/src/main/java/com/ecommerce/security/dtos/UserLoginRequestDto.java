package com.ecommerce.security.dtos;

import lombok.Data;

@Data
public class UserLoginRequestDto {  // Renamed from UserLoginDto
    private String emailOrPhone; // Flexible for both email or phone
    private String password;
}

