package com.ecommerce.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class UserProfileDto {
	private Long id;
    private String name;
    private String email;
    private String phone;
    private String gender; // New field for gender
    private LocalDate dob; // New field for date of birth
}