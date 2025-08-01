package com.femi.tickerdesk.enumFolder;

import lombok.Getter;

@Getter
public enum TicketCategory {
    ACCOUNT_MANAGEMENT("Account Management"),
    APPLICATIONS("Applications"),
    FACILITIES("Facilities"),
    FINANCE("Finance"),
    HARDWARE("Hardware"),
    HUMAN_RESOURCES("Human Resources"),
    NETWORKING("Networking");

    private final String displayName;

    TicketCategory(String displayName) {
        this.displayName = displayName;
    }

}
