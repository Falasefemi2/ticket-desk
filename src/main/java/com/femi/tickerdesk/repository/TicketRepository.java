package com.femi.tickerdesk.repository;

import com.femi.tickerdesk.enumFolder.Priority;
import com.femi.tickerdesk.enumFolder.TicketCategory;
import com.femi.tickerdesk.enumFolder.TicketStatus;
import com.femi.tickerdesk.model.Ticket;
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
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByCreatedBy(User createdBy);

    List<Ticket> findByAssignedTo(User assignedTo);

    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByCategory(TicketCategory category);

    List<Ticket> findByPriority(Priority priority);

    Page<Ticket> findByCreatedBy(User createdBy, Pageable pageable);

    Page<Ticket> findByAssignedTo(User assignedTo, Pageable pageable);

    Page<Ticket> findByStatus(TicketStatus status, Pageable pageable);

    Page<Ticket> findByCategory(TicketCategory category, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.createdBy.department = :department")
    List<Ticket> findByCreatedByDepartment(@Param("department") String department);

    @Query("SELECT t FROM Ticket t WHERE t.status IN :statuses")
    List<Ticket> findByStatusIn(@Param("statuses") List<TicketStatus> statuses);

    @Query("SELECT t FROM Ticket t WHERE t.title LIKE %:keyword% OR t.description LIKE %:keyword%")
    Page<Ticket> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    List<Ticket> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = :status")
    Long countByStatus(@Param("status") TicketStatus status);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.assignedTo = :user AND t.status IN :statuses")
    Long countAssignedTicketsByStatus(@Param("user") User user, @Param("statuses") List<TicketStatus> statuses);

    @Query("SELECT t FROM Ticket t WHERE t.assignedTo IS NULL AND t.status = 'OPEN'")
    List<Ticket> findUnassignedOpenTickets();

    @Query("SELECT t FROM Ticket t WHERE t.priority = 'URGENT' AND t.status IN ('OPEN', 'IN_PROGRESS')")
    List<Ticket> findUrgentActiveTickets();

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.createdBy = :user")
    Long countTicketsCreatedByUser(@Param("user") User user);
}