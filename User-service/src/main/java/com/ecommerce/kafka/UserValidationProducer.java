package com.ecommerce.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.UserValidationResponseDto;

@Service
public class UserValidationProducer {

    @Autowired
    private KafkaTemplate<String, UserValidationResponseDto> kafkaTemplate;

    public void sendValidationResponse(UserValidationResponseDto responseDto) {
        System.out.println("User Microservice - Producer Sending: " + responseDto);
        kafkaTemplate.send("user-validation-response", responseDto);
    }
}
