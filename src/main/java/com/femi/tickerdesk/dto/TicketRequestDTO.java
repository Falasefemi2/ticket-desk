package com.femi.tickerdesk.dto;

import com.femi.tickerdesk.enumFolder.Priority;
import com.femi.tickerdesk.enumFolder.TicketCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDTO {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private TicketCategory category;

    private Priority priority = Priority.MEDIUM;

    @NotNull
    private Long createdById;

    private Long assignedToId;

    private Long serviceCatalogItemId;

    private String additionalData;

    private String ccEmails;
}
