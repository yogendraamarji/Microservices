package com.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final String VERIFICATION_TOPIC = "email-verification-topic";  // Kafka topic name for verification
    private static final String PASSWORD_RESET_TOPIC = "password-reset-topic";  // Kafka topic name for password reset

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Send a verification email
    public void sendVerificationEmail(String email, String verificationCode) {
        String message = "Verification Code: " + verificationCode + " for email: " + email;
        kafkaTemplate.send(VERIFICATION_TOPIC, message);  // Send the message to the verification topic
    }

    // Send a password reset email
    public void sendPasswordResetEmail(String email, String resetCode) {
        String message = "Password Reset Code: " + resetCode + " for email: " + email;
        kafkaTemplate.send(PASSWORD_RESET_TOPIC, message);  // Send the message to the password reset topic
    }
}
