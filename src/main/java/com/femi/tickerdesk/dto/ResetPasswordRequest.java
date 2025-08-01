package com.femi.tickerdesk.dto;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class ResetPasswordRequest {

    @jakarta.validation.constraints.NotBlank(message = "Token is required")
    private String token;

    @jakarta.validation.constraints.NotBlank(message = "New password is required")
    @jakarta.validation.constraints.Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;
}
