package com.ecommerce.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String phone;
    private String gender;
    private LocalDate dob;
    private String role; // New field to select the role during registration

}