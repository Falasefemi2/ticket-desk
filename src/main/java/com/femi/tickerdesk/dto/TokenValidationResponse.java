package com.femi.tickerdesk.dto;

@lombok.Data
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class TokenValidationResponse {

    private Boolean valid;
    private String email;
    private java.util.Date expiresAt;

}
