package com.femi.tickerdesk.dto;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class UpdateProfileRequest {
    @jakarta.validation.constraints.NotBlank(message = "Email is required")
    @jakarta.validation.constraints.Email(message = "Please provide a valid email")
    private String email;

    @jakarta.validation.constraints.NotBlank(message = "First name is required")
    @jakarta.validation.constraints.Size(min = 2, max = 50)
    private String firstName;

    @jakarta.validation.constraints.NotBlank(message = "Last name is required")
    @jakarta.validation.constraints.Size(min = 2, max = 50)
    private String lastName;
}