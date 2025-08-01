package com.femi.tickerdesk.enumFolder;

import lombok.Getter;

@Getter
public enum Role {
    USER("Can create and view own tickets"),
    TECHNICIAN("Can be assigned and resolve tickets"),
    MANAGER("Can approve and manage department tickets"),
    ADMIN("Full system access");

    private final String description;

    Role(String description) {
        this.description = description;
    }

}