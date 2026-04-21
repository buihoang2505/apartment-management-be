package com.apartment.app.audit.handler;

import com.apartment.app.audit.dto.AuditLogResponse;
import com.apartment.app.audit.query.AuditLogQuery;
import com.apartment.domain.audit.AuditLog;
import com.apartment.domain.audit.AuditLogRepository;
import com.apartment.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditLogQueryHandler {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public Page<AuditLogResponse> findAll(int page, int size, String callerUsername, String callerRole) {
        var pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        if ("MANAGER".equals(callerRole)) {
            UUID callerId = userRepository.findByUsername(callerUsername)
                    .map(u -> u.getId())
                    .orElse(null);
            if (callerId == null) return Page.empty(pageable);
            return auditLogRepository.findByUser_IdOrderByCreatedAtDesc(callerId, pageable)
                    .map(AuditLogResponse::from);
        }

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

    public List<AuditLogResponse> findByUser(UUID userId, String callerUsername, String callerRole) {
        if ("MANAGER".equals(callerRole)) {
            UUID callerId = userRepository.findByUsername(callerUsername)
                    .map(u -> u.getId())
                    .orElse(null);
            // MANAGER chỉ được xem log của chính mình
            if (!userId.equals(callerId)) return List.of();
        }
        return auditLogRepository.findByUser_IdOrderByCreatedAtDesc(userId)
                .stream()
                .map(AuditLogResponse::from)
                .toList();
    }

    /**
     * Admin endpoint: paginated search with optional filters.
     * The Specification is built in the infra/controller layer and passed in
     * to keep this handler free of infra dependencies.
     */
    public Page<AuditLogResponse> query(AuditLogQuery q, Specification<AuditLog> spec) {
        var pageable = PageRequest.of(q.page(), q.size(), Sort.by("createdAt").descending());
        return auditLogRepository.findAll(spec, pageable).map(AuditLogResponse::from);
    }
}
