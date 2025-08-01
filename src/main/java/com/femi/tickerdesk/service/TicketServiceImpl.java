package com.femi.tickerdesk.service;

import com.femi.tickerdesk.enumFolder.Priority;
import com.femi.tickerdesk.enumFolder.TicketCategory;
import com.femi.tickerdesk.enumFolder.TicketStatus;
import com.femi.tickerdesk.model.Ticket;
import com.femi.tickerdesk.model.User;
import com.femi.tickerdesk.repository.TicketRepository;
import com.femi.tickerdesk.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;


    @Override
    public Ticket createTicket(Ticket ticket) {
        Long creatorId = ticket.getCreatedBy().getId();

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException("Creator user not found"));

        ticket.setCreatedBy(creator);

        if (ticket.getAssignedTo() != null && ticket.getAssignedTo().getId() != null) {
            Long assigneeId = ticket.getAssignedTo().getId();
            User assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new EntityNotFoundException("Assignee user not found"));
            ticket.setAssignedTo(assignee);
        }

        if (ticket.getStatus() == null) {
            ticket.setStatus(TicketStatus.OPEN);
        }

        if (ticket.getPriority() == null) {
            ticket.setPriority(Priority.MEDIUM);
        }

        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket updateTicket(Long id, Ticket ticket) {
        return null;
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Ticket> findAll() {
        return List.of();
    }

    @Override
    public Page<Ticket> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Ticket> findByCreatedBy(User createdBy) {
        return List.of();
    }

    @Override
    public Page<Ticket> findByCreatedBy(User createdBy, Pageable pageable) {
        return null;
    }

    @Override
    public List<Ticket> findByAssignedTo(User assignedTo) {
        return List.of();
    }

    @Override
    public Page<Ticket> findByAssignedTo(User assignedTo, Pageable pageable) {
        return null;
    }

    @Override
    public List<Ticket> findByStatus(TicketStatus status) {
        return List.of();
    }

    @Override
    public Page<Ticket> findByStatus(TicketStatus status, Pageable pageable) {
        return null;
    }

    @Override
    public List<Ticket> findByCategory(TicketCategory category) {
        return List.of();
    }

    @Override
    public Page<Ticket> findByCategory(TicketCategory category, Pageable pageable) {
        return null;
    }

    @Override
    public List<Ticket> findByCreatedByDepartment(String department) {
        return List.of();
    }

    @Override
    public List<Ticket> findByStatusIn(List<TicketStatus> statuses) {
        return List.of();
    }

    @Override
    public Page<Ticket> searchByKeyword(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public List<Ticket> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return List.of();
    }

    @Override
    public Ticket assignTicket(Long ticketId, Long userId) {
        return null;
    }

    @Override
    public Ticket unassignTicket(Long ticketId) {
        return null;
    }

    @Override
    public Ticket updateTicketStatus(Long ticketId, TicketStatus status) {
        return null;
    }

    @Override
    public Ticket updateTicketPriority(Long ticketId, Priority priority) {
        return null;
    }

    @Override
    public Ticket resolveTicket(Long ticketId) {
        return null;
    }

    @Override
    public Ticket closeTicket(Long ticketId) {
        return null;
    }

    @Override
    public Ticket reopenTicket(Long ticketId) {
        return null;
    }

    @Override
    public void deleteTicket(Long id) {

    }

    @Override
    public Long countByStatus(TicketStatus status) {
        return 0L;
    }

    @Override
    public Long countAssignedTicketsByStatus(User user, List<TicketStatus> statuses) {
        return 0L;
    }

    @Override
    public List<Ticket> findUnassignedOpenTickets() {
        return List.of();
    }

    @Override
    public List<Ticket> findUrgentActiveTickets() {
        return List.of();
    }

    @Override
    public Long countTicketsCreatedByUser(User user) {
        return 0L;
    }

    @Override
    public Ticket autoAssignTicket(Long ticketId) {
        return null;
    }
}
