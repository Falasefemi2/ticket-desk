package com.femi.tickerdesk.controller;

import com.femi.tickerdesk.dto.ApiResponse;
import com.femi.tickerdesk.dto.TicketRequestDTO;
import com.femi.tickerdesk.model.ServiceCatalogItem;
import com.femi.tickerdesk.model.Ticket;
import com.femi.tickerdesk.model.User;
import com.femi.tickerdesk.repository.ServiceCatalogItemRepository;
import com.femi.tickerdesk.repository.UserRepository;
import com.femi.tickerdesk.service.TicketService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;
    private final ServiceCatalogItemRepository serviceCatalogRepository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<Ticket>> createTicket(@Valid @RequestBody TicketRequestDTO request) {
        Ticket ticket = new Ticket();

        // Basic fields
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setCategory(request.getCategory());
        ticket.setPriority(request.getPriority());
        ticket.setAdditionalData(request.getAdditionalData());
        ticket.setCcEmails(request.getCcEmails());

        // Attach creator
        User createdBy = userRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new EntityNotFoundException("Creator not found"));
        ticket.setCreatedBy(createdBy);

        // Optional: Attach assignee
        if (request.getAssignedToId() != null) {
            User assignedTo = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            ticket.setAssignedTo(assignedTo);
        }

        // Optional: Attach serviceCatalogItem
        if (request.getServiceCatalogItemId() != null) {
            ServiceCatalogItem item = serviceCatalogRepository.findById(request.getServiceCatalogItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Catalog Item not found"));
            ticket.setServiceCatalogItem(item);
        }

        Ticket savedTicket = ticketService.createTicket(ticket);


        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Ticket created", savedTicket));
    }


}
