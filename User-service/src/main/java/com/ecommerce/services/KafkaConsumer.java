package com.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private final EmailSenderService emailSenderService;

    @Autowired
    public KafkaConsumer(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    // Listener for the email verification Kafka topic
    @KafkaListener(topics = "email-verification-topic", groupId = "email-group")
    public void listen(String message) {
        // Extract the email and verification code from the message
        String[] parts = message.split(" for email: ");
        String verificationCode = parts[0].replace("Verification Code: ", "").trim();
        String email = parts[1].trim();

        // Send the email using the email service
        emailSenderService.sendVerificationCode(email, verificationCode);
    }
}
