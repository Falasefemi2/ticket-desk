package com.femi.tickerdesk.dto;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class RefreshTokenRequest {

    @jakarta.validation.constraints.NotBlank(message = "Token is required")
    private String token;
}
