package com.femi.tickerdesk.dto;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class ForgotPasswordRequest {

    @jakarta.validation.constraints.NotBlank(message = "Email is required")
    @jakarta.validation.constraints.Email(message = "Please provide a valid email")
    private String email;
}
