package com.femi.tickerdesk.dto;

import com.femi.tickerdesk.enumFolder.Department;
import com.femi.tickerdesk.enumFolder.Role;
import com.femi.tickerdesk.enumFolder.Site;
import com.femi.tickerdesk.model.User;

import java.time.LocalDateTime;

@lombok.Data
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class UserInfo {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private Department department;
    private Site site;
    private Role role;
    private String employeeId;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public static UserInfo fromUser(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .department(user.getDepartment())
                .site(user.getSite())
                .role(user.getRole())
                .employeeId(user.getEmployeeId())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .build();
    }
}
