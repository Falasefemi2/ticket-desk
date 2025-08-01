package com.femi.tickerdesk.dto;

import com.femi.tickerdesk.enumFolder.Department;
import com.femi.tickerdesk.enumFolder.Role;
import com.femi.tickerdesk.enumFolder.Site;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class RegisterRequest {

    @jakarta.validation.constraints.NotBlank(message = "Email is required")
    @jakarta.validation.constraints.Email(message = "Please provide a valid email")
    private String email;

    @jakarta.validation.constraints.NotBlank(message = "First name is required")
    @jakarta.validation.constraints.Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @jakarta.validation.constraints.NotBlank(message = "Last name is required")
    @jakarta.validation.constraints.Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @jakarta.validation.constraints.NotBlank(message = "Password is required")
    @jakarta.validation.constraints.Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @jakarta.validation.constraints.NotNull(message = "Department is required")
    private Department department;

    @jakarta.validation.constraints.NotNull(message = "Site is required")
    private Site site;

    private Role role;

    private String employeeId;
}
