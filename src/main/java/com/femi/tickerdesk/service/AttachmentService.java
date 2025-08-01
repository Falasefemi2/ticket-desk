package com.femi.tickerdesk.service;

import com.femi.tickerdesk.model.Attachment;
import com.femi.tickerdesk.model.Ticket;
import com.femi.tickerdesk.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttachmentService {

    Attachment uploadAttachment(MultipartFile file, Ticket ticket, User uploadedBy);

    Attachment createAttachment(Attachment attachment);

    Optional<Attachment> findById(Long id);

    Optional<Attachment> findByFileName(String fileName);

    List<Attachment> findAll();

    List<Attachment> findByTicket(Ticket ticket);

    List<Attachment> findByUploadedBy(User uploadedBy);

    List<Attachment> findByTicketOrderByUploadedAtDesc(Ticket ticket);

    List<Attachment> findImageAttachments();

    List<Attachment> findLargeFiles(Long size);

    byte[] downloadAttachment(Long attachmentId);

    void deleteAttachment(Long id);

    Long getTotalFileSizeByTicket(Ticket ticket);

    Long countAttachmentsByTicket(Ticket ticket);

    List<Attachment> findByUploadedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    Long countAttachmentsByUser(User user);

    boolean existsByFileName(String fileName);

    String generateUniqueFileName(String originalFileName);
}