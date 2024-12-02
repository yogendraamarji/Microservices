package com.ecommerce.security.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.security.dtos.JwtResponse;
import com.ecommerce.security.dtos.UserLoginRequestDto;
import com.ecommerce.security.services.CustomUserDetailsService;
import com.ecommerce.security.utils.JwtTokenService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserLoginRequestDto loginDto) {
        // Authenticate the user using the AuthenticationManager
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(loginDto.getEmailOrPhone(), loginDto.getPassword());

        authenticationManager.authenticate(authentication);

        // Load user details from the CustomUserDetailsService using email or phone (not the entire DTO)
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginDto.getEmailOrPhone());

        // If authentication is successful, generate JWT token
        String jwt = jwtTokenService.generateToken(userDetails); // Passing UserDetails here

        // Return the JWT in the response
        return ResponseEntity.ok(new JwtResponse(jwt)); // Ensure JwtResponse exists
    }
}