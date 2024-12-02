package com.ecommerce.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ecommerce.dto.UserValidationRequestDto;
import com.ecommerce.dto.UserValidationResponseDto;
import com.ecommerce.entities.Role;
import com.ecommerce.entities.User;
import com.ecommerce.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserValidationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserValidationConsumer.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidationProducer producer;

    @KafkaListener(topics = "user-validation-request", groupId = "user-validation-group")
    public void listen(UserValidationRequestDto requestDto) {
        logger.info("Received validation request: CorrelationId={}", requestDto.getCorrelationId());

        UserValidationResponseDto responseDto = new UserValidationResponseDto();
        responseDto.setCorrelationId(requestDto.getCorrelationId());

        try {
            boolean isValid = userService.validateUserCredentials(requestDto);
            
            if (isValid) {
                User user = userService.findUserByEmailOrPhoneAndPassword(requestDto.getEmailOrPhone(), requestDto.getPassword());
                responseDto.setSuccess(true);
                responseDto.setName(user.getName());
                responseDto.setEmail(user.getEmail());
                responseDto.setRoles(user.getRoles().stream().map(Role::getName).toList());
                responseDto.setMessage("Validation successful");
            } else {
                responseDto.setSuccess(false);
                responseDto.setMessage("Invalid credentials");
            }

            logger.info("Sending validation response: CorrelationId={}, Success={}", responseDto.getCorrelationId(), responseDto.isSuccess());
            producer.sendValidationResponse(responseDto);

        } catch (Exception e) {
            logger.error("Error processing validation request: CorrelationId={}, Error={}", requestDto.getCorrelationId(), e.getMessage(), e);
            responseDto.setSuccess(false);
            responseDto.setMessage("Internal error during validation");
            producer.sendValidationResponse(responseDto);
        }
    }
}
