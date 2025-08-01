package com.femi.tickerdesk.service;

import com.femi.tickerdesk.enumFolder.Priority;
import com.femi.tickerdesk.enumFolder.TicketCategory;
import com.femi.tickerdesk.enumFolder.TicketStatus;
import com.femi.tickerdesk.model.Ticket;
import com.femi.tickerdesk.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketService {

    Ticket createTicket(Ticket ticket);

    Ticket updateTicket(Long id, Ticket ticket);

    Optional<Ticket> findById(Long id);

    List<Ticket> findAll();

    Page<Ticket> findAll(Pageable pageable);

    List<Ticket> findByCreatedBy(User createdBy);

    Page<Ticket> findByCreatedBy(User createdBy, Pageable pageable);

    List<Ticket> findByAssignedTo(User assignedTo);

    Page<Ticket> findByAssignedTo(User assignedTo, Pageable pageable);

    List<Ticket> findByStatus(TicketStatus status);

    Page<Ticket> findByStatus(TicketStatus status, Pageable pageable);

    List<Ticket> findByCategory(TicketCategory category);

    Page<Ticket> findByCategory(TicketCategory category, Pageable pageable);

    List<Ticket> findByCreatedByDepartment(String department);

    List<Ticket> findByStatusIn(List<TicketStatus> statuses);

    Page<Ticket> searchByKeyword(String keyword, Pageable pageable);

    List<Ticket> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    Ticket assignTicket(Long ticketId, Long userId);

    Ticket unassignTicket(Long ticketId);

    Ticket updateTicketStatus(Long ticketId, TicketStatus status);

    Ticket updateTicketPriority(Long ticketId, Priority priority);

    Ticket resolveTicket(Long ticketId);

    Ticket closeTicket(Long ticketId);

    Ticket reopenTicket(Long ticketId);

    void deleteTicket(Long id);

    Long countByStatus(TicketStatus status);

    Long countAssignedTicketsByStatus(User user, List<TicketStatus> statuses);

    List<Ticket> findUnassignedOpenTickets();

    List<Ticket> findUrgentActiveTickets();

    Long countTicketsCreatedByUser(User user);

    Ticket autoAssignTicket(Long ticketId);
}
