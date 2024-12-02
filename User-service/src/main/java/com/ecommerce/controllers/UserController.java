package com.ecommerce.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.ForgotPasswordDto;
import com.ecommerce.dto.ResetPasswordDto;
import com.ecommerce.dto.UserProfileDto;
import com.ecommerce.dto.UserRegistrationDto;
import com.ecommerce.dto.UserResponseDto;
import com.ecommerce.dto.UserValidationRequestDto;
import com.ecommerce.dto.VerificationDto;
import com.ecommerce.entities.Role;
import com.ecommerce.entities.User;
import com.ecommerce.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            User newUser = userService.registerUser(registrationDto);
            Set<Role> roles = newUser.getRoles();
            UserResponseDto userResponseDto = new UserResponseDto(newUser.getId(), newUser.getEmail(), newUser.getPassword(), newUser.getName(), roles);
            return ResponseEntity.ok(userResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Verify user account with 6-digit code
    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody VerificationDto verificationDto) {
        try {
            userService.verifyUserByCode(verificationDto.getCode());
            return ResponseEntity.ok("User successfully verified.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Send password reset code to email
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        try {
            userService.sendPasswordResetEmail(forgotPasswordDto.getEmail());
            return ResponseEntity.ok("Password reset code sent to your email.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Reset password with the 6-digit code
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        try {
            userService.resetPassword(resetPasswordDto.getCode(), resetPasswordDto.getNewPassword());
            return ResponseEntity.ok("Password reset successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // User validation for login functionality
    @PostMapping("/validate")
    public ResponseEntity<ApiResponse> validateUser(@RequestBody UserValidationRequestDto loginDto) {
        boolean isValid = userService.validateUserCredentials(loginDto);
        if (isValid) {
            return ResponseEntity.ok(new ApiResponse(true, "Login successful"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Invalid credentials"));
        }
    }


    // Update the user's details (except password)
    @PutMapping("/update/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId, @RequestBody UserProfileDto updateDto) {
        try {
            User updatedUser = userService.updateUser(userId, updateDto);
            Set<Role> roles = updatedUser.getRoles();
            UserResponseDto userResponseDto = new UserResponseDto(updatedUser.getId(), updatedUser.getEmail(), updatedUser.getPassword(), updatedUser.getName(), roles);
            return ResponseEntity.ok(userResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // View the user's details by ID
    @GetMapping("/view/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            Set<Role> roles = user.getRoles();
            UserResponseDto userResponseDto = new UserResponseDto(user.getId(), user.getEmail(), user.getPassword(), user.getName(), roles);
            return ResponseEntity.ok(userResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
