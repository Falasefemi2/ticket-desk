package com.femi.tickerdesk.service;

import com.femi.tickerdesk.model.Ticket;
import com.femi.tickerdesk.model.TicketComment;
import com.femi.tickerdesk.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketCommentService {

    TicketComment createComment(TicketComment comment);

    TicketComment updateComment(Long id, TicketComment comment);

    Optional<TicketComment> findById(Long id);

    List<TicketComment> findAll();

    List<TicketComment> findByTicket(Ticket ticket);

    List<TicketComment> findByTicketOrderByCreatedAtAsc(Ticket ticket);

    List<TicketComment> findByTicketOrderByCreatedAtDesc(Ticket ticket);

    List<TicketComment> findByUser(User user);

    Page<TicketComment> findByTicket(Ticket ticket, Pageable pageable);

    List<TicketComment> findByTicketAndIsInternal(Ticket ticket, Boolean isInternal);

    List<TicketComment> findPublicCommentsByTicket(Ticket ticket);

    TicketComment addCommentToTicket(Long ticketId, String comment, Long userId, Boolean isInternal);

    void deleteComment(Long id);

    Long countCommentsByTicket(Ticket ticket);

    List<TicketComment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    Long countCommentsByUser(User user);
}
