package com.ecommerce.security.dtos;

import java.util.List;

import lombok.Data;

@Data
public class UserValidationResponseDto {
    private String correlationId;
    private boolean success;
    private String name;  // Optional: can be null if not available
    private String email; // Optional: can be null if not available
    private String message;
    private List<String> roles; // Changed to List<String> for flexibility
}
