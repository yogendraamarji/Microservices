package com.ecommerce.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.UserProfileDto;
import com.ecommerce.dto.UserRegistrationDto;
import com.ecommerce.dto.UserValidationRequestDto;
import com.ecommerce.entities.Role;
import com.ecommerce.entities.User;
import com.ecommerce.entities.VerificationToken;
import com.ecommerce.repositories.RoleRepository;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.repositories.VerificationTokenRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final KafkaProducer kafkaProducer;

    @Autowired
    public UserService(KafkaProducer kafkaProducer, UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, VerificationTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.kafkaProducer = kafkaProducer;
    }

    // Generate 6-digit verification/reset code
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    // Register a new user and send a verification code
    public User registerUser(UserRegistrationDto registrationDto) {
        Optional<User> existingUser = userRepository.findByEmail(registrationDto.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }

        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setPhone(registrationDto.getPhone());
        user.setGender(registrationDto.getGender());
        user.setDob(registrationDto.getDob());
        user.setEnabled(false);  // User is disabled until email is verified

        Optional<Role> userRole = roleRepository.findByName(registrationDto.getRole().toUpperCase());
        if (userRole.isPresent()) {
            user.getRoles().add(userRole.get());
        } else {
            throw new RuntimeException("Role not found: " + registrationDto.getRole());
        }

        userRepository.save(user);

        // Generate and save verification code
        String verificationCode = generateVerificationCode();
        VerificationToken verificationToken = new VerificationToken(user, verificationCode);
        tokenRepository.save(verificationToken);

        // Send the verification code via Kafka (email sending)
        kafkaProducer.sendVerificationEmail(user.getEmail(), verificationCode);

        return user;
    }

    // Verify the user account by 6-digit code
    public void verifyUserByCode(String code) {
        VerificationToken token = tokenRepository.findByToken(code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification code."));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification code has expired.");
        }

        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenRepository.delete(token);  // Optionally delete the token after verification
    }

    // Send password reset email with verification code
    public void sendPasswordResetEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        Optional<VerificationToken> existingToken = tokenRepository.findByUser(user);
        existingToken.ifPresent(tokenRepository::delete);

        String resetCode = generateVerificationCode();
        VerificationToken resetToken = new VerificationToken(user, resetCode);
        tokenRepository.save(resetToken);

        kafkaProducer.sendPasswordResetEmail(user.getEmail(), resetCode);
    }

    // Reset the password using the verification code
    public void resetPassword(String enteredCode, String newPassword) {
        VerificationToken token = tokenRepository.findByToken(enteredCode)
                .orElseThrow(() -> new IllegalArgumentException("Reset code not found."));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset code has expired.");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public boolean validateUserCredentials(UserValidationRequestDto loginDto) {
        // Extract email or phone and password from the DTO
        String emailOrPhone = loginDto.getEmailOrPhone();
        String password = loginDto.getPassword();

        // Validate the user credentials using your existing method
        return findUserByEmailOrPhoneAndPassword(emailOrPhone, password) != null;
    }

    // Find user by email or phone and validate password
    public User findUserByEmailOrPhoneAndPassword(String emailOrPhone, String password) {
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return null;
        }

        emailOrPhone = emailOrPhone.trim();

        Optional<User> userOpt = userRepository.findByEmail(emailOrPhone.toLowerCase());
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByPhone(emailOrPhone);
        }

        return userOpt.filter(user -> passwordEncoder.matches(password, user.getPassword()))
                      .orElse(null);
    }

    // Update the user's details (except password)
    public User updateUser(Long userId, UserProfileDto updateDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // Update the fields that can be modified
        if (updateDto.getName() != null) existingUser.setName(updateDto.getName());
        if (updateDto.getEmail() != null) existingUser.setEmail(updateDto.getEmail());
        if (updateDto.getPhone() != null) existingUser.setPhone(updateDto.getPhone());
        if (updateDto.getGender() != null) existingUser.setGender(updateDto.getGender());
        if (updateDto.getDob() != null) existingUser.setDob(updateDto.getDob());

        userRepository.save(existingUser);
        return existingUser;
    }

    // View the user's details by ID
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }
}
