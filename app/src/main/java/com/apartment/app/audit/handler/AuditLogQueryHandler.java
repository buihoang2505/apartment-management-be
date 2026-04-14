package com.apartment.app.audit.handler;

import com.apartment.app.audit.dto.AuditLogResponse;
import com.apartment.domain.audit.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditLogQueryHandler {

    private final AuditLogRepository auditLogRepository;

    public Page<AuditLogResponse> findAll(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return auditLogRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(AuditLogResponse::from);
    }

    public List<AuditLogResponse> findByEntity(String entityType, String entityId) {
        return auditLogRepository
                .findByEntityTypeAndEntityIdOrderByCreatedAtDesc(entityType, entityId)
                .stream()
                .map(AuditLogResponse::from)
                .toList();
    }

    public List<AuditLogResponse> findByUser(UUID userId) {
        return auditLogRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(AuditLogResponse::from)
                .toList();
    }
}