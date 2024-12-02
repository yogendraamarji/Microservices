package com.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    // Send a 6-digit verification code
    public void sendVerificationCode(String email, String code) {
        String subject = "Email Verification";
        String message = "Your 6-digit verification code is: " + code;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }

    // Send a 6-digit password reset code
    public void sendPasswordResetCode(String email, String code) {
        String subject = "Password Reset Request";
        String message = "Your 6-digit password reset code is: " + code;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}
