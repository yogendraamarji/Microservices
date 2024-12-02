package com.ecommerce.security.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.ecommerce.security.dtos.UserValidationRequestDto;

@Component
public class CentralizedSecurityProducer {

    private static final String USER_VALIDATION_REQUEST_TOPIC = "user-validation-request";

    @Autowired
    private KafkaTemplate<String, UserValidationRequestDto> kafkaTemplate;

    public void sendValidationRequest(UserValidationRequestDto validationRequest) {
        // Log outgoing message
        System.out.println("Centralized Security Microservice - Sending: " + validationRequest);
        kafkaTemplate.send(USER_VALIDATION_REQUEST_TOPIC, validationRequest);
    }
}

