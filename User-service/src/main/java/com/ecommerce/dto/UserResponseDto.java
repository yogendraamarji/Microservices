package com.ecommerce.dto;

import java.util.Set;

import com.ecommerce.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
	private Long id;         // User ID
    private String email;    // User email
    private String password; // User password (optional, if needed)
    private String name;     // User name
    private Set<Role> roles; // Set of roles
}
