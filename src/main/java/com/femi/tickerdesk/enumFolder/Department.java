package com.femi.tickerdesk.enumFolder;

import lombok.Getter;

@Getter
public enum Department {
    FINANCE("Financial operations and requests"),
    HR_ADMIN("Human resources and administration"),
    MARKETING("Marketing and communications"),
    SYSTEM_NETWORK("IT systems and network management");

    private final String description;

    Department(String description) {
        this.description = description;
    }

}