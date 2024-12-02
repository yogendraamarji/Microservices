package com.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Include non-null fields only
@JsonPropertyOrder({"correlationId", "emailOrPhone", "password"})
public class UserValidationRequestDto {
	 private String correlationId;
	    private String emailOrPhone;
	    private String password;
}
