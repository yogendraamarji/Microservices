package com.ecommerce.security.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ecommerce.security.dtos.UserValidationResponseDto;
import com.ecommerce.security.utils.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CentralizedSecurityConsumer {

    @Autowired
    private JwtTokenService jwtTokenService;

    @KafkaListener(topics = "user-validation-response", groupId = "user-validation-group")
    public void listenForValidationResponse(UserValidationResponseDto responseDto) {
        // Log incoming message
        System.out.println("Centralized Security Microservice - Received: " + responseDto);

        if (responseDto.isSuccess()) {
            String token = jwtTokenService.generateTokenFromResponseDto(responseDto.getEmail(), responseDto.getRoles());
            System.out.println("JWT Token Generated: " + token);
        } else {
            System.out.println("User validation failed: " + responseDto.getMessage());
        }
    }
}
