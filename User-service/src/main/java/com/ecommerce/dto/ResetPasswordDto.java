package com.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {
    private String code; // The token sent to the user's email
    private String newPassword; // The new password

}

