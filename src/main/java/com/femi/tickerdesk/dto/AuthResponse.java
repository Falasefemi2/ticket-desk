package com.femi.tickerdesk.dto;

@lombok.Data
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AuthResponse {

    private String token;
    private String tokenType;
    private Long expiresIn; // in seconds
    private UserInfo user;
}
