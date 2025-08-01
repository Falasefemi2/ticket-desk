package com.femi.tickerdesk.model;

import com.femi.tickerdesk.enumFolder.Priority;
import com.femi.tickerdesk.enumFolder.TicketCategory;
import com.femi.tickerdesk.enumFolder.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.OPEN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketCategory category;

    // Relationship with User (Creator)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    // Relationship with User (Assignee)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    // Relationship with ServiceCatalogItem
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_catalog_item_id")
    private ServiceCatalogItem serviceCatalogItem;

    // One-to-Many relationship with TicketComment
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TicketComment> comments = new ArrayList<>();

    // One-to-Many relationship with Attachment
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Attachment> attachments = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime resolvedAt;

    // Additional fields for specific request types (JSON storage for flexibility)
    @Column(columnDefinition = "TEXT")
    private String additionalData; // JSON string for dynamic fields

    // CC (Carbon Copy) for notifications
    @Column
    private String ccEmails; // Comma-separated email list

    // Helper method to generate ticket number
    public String getTicketNumber() {
        return "TKT-" + String.format("%06d", id);
    }

    // Helper method to add comment
    public void addComment(TicketComment comment) {
        comments.add(comment);
        comment.setTicket(this);
    }

    // Helper method to add attachment
    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
        attachment.setTicket(this);
    }
}
