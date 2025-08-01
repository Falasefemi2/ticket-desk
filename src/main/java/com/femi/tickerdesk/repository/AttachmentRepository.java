package com.femi.tickerdesk.repository;

import com.femi.tickerdesk.model.Attachment;
import com.femi.tickerdesk.model.Ticket;
import com.femi.tickerdesk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByTicket(Ticket ticket);

    List<Attachment> findByUploadedBy(User uploadedBy);

    Optional<Attachment> findByFileName(String fileName);

    List<Attachment> findByTicketOrderByUploadedAtDesc(Ticket ticket);

    @Query("SELECT a FROM Attachment a WHERE a.contentType LIKE 'image%'")
    List<Attachment> findImageAttachments();

    @Query("SELECT a FROM Attachment a WHERE a.fileSize > :size")
    List<Attachment> findLargeFiles(@Param("size") Long size);

    @Query("SELECT SUM(a.fileSize) FROM Attachment a WHERE a.ticket = :ticket")
    Long getTotalFileSizeByTicket(@Param("ticket") Ticket ticket);

    @Query("SELECT COUNT(a) FROM Attachment a WHERE a.ticket = :ticket")
    Long countAttachmentsByTicket(@Param("ticket") Ticket ticket);

    @Query("SELECT a FROM Attachment a WHERE a.uploadedAt BETWEEN :startDate AND :endDate")
    List<Attachment> findByUploadedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(a) FROM Attachment a WHERE a.uploadedBy = :user")
    Long countAttachmentsByUser(@Param("user") User user);

    boolean existsByFileName(String fileName);
}
