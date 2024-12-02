package com.ecommerce.security.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentialDto {  // Renamed from UserDetailsDto
    private String email;
    private String password;
    private List<String> roles;
}
