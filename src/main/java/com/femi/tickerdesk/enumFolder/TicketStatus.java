package com.femi.tickerdesk.enumFolder;

import lombok.Getter;

@Getter
public enum TicketStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    WAITING_FOR_APPROVAL("Waiting for Approval"),
    WAITING_FOR_USER("Waiting for User Response"),
    RESOLVED("Resolved"),
    CLOSED("Closed"),
    CANCELLED("Cancelled");

    private final String displayName;

    TicketStatus(String displayName) {
        this.displayName = displayName;
    }

}
