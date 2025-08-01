package com.femi.tickerdesk.repository;

import com.femi.tickerdesk.model.Ticket;
import com.femi.tickerdesk.model.TicketComment;
import com.femi.tickerdesk.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {

    List<TicketComment> findByTicket(Ticket ticket);

    List<TicketComment> findByTicketOrderByCreatedAtAsc(Ticket ticket);

    List<TicketComment> findByTicketOrderByCreatedAtDesc(Ticket ticket);

    List<TicketComment> findByUser(User user);

    Page<TicketComment> findByTicket(Ticket ticket, Pageable pageable);

    @Query("SELECT c FROM TicketComment c WHERE c.ticket = :ticket AND c.isInternal = :isInternal ORDER BY c.createdAt ASC")
    List<TicketComment> findByTicketAndIsInternal(@Param("ticket") Ticket ticket, @Param("isInternal") Boolean isInternal);

    @Query("SELECT c FROM TicketComment c WHERE c.ticket = :ticket AND c.isInternal = false ORDER BY c.createdAt ASC")
    List<TicketComment> findPublicCommentsByTicket(@Param("ticket") Ticket ticket);

    @Query("SELECT COUNT(c) FROM TicketComment c WHERE c.ticket = :ticket")
    Long countCommentsByTicket(@Param("ticket") Ticket ticket);

    @Query("SELECT c FROM TicketComment c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<TicketComment> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(c) FROM TicketComment c WHERE c.user = :user")
    Long countCommentsByUser(@Param("user") User user);
}
