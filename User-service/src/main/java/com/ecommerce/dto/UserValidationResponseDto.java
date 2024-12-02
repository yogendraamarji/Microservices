package com.ecommerce.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Include non-null fields only
@JsonPropertyOrder({"correlationId", "success", "name", "email", "roles", "message"})
public class UserValidationResponseDto {
    private String correlationId;
    private boolean success;
    private String name;  // Optional: will be used for token generation
    private String email;
    private List<String> roles; // Changed to List<String> for better flexibility
    private String message; // For success/failure feedback
}
