package com.apartment.app.audit.dto;

import com.apartment.domain.audit.AuditLog;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditLogResponse(
        UUID id,
        String username,
        String action,
        String entityType,
        String entityId,
        String oldValue,
        String newValue,
        String ipAddress,
        LocalDateTime createdAt
) {
    public static AuditLogResponse from(AuditLog log) {
        return new AuditLogResponse(
                log.getId(),
                log.getUser() != null ? log.getUser().getUsername() : null,
                log.getAction(),
                log.getEntityType(),
                log.getEntityId(),
                log.getOldValue(),
                log.getNewValue(),
                log.getIpAddress(),
                log.getCreatedAt()
        );
    }
}