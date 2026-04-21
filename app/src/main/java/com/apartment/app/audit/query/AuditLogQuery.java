package com.apartment.app.audit.query;

import java.util.UUID;

public record AuditLogQuery(
                UUID userId,
                String action,
                String entityType,
                String fromDate,
                String toDate,
                String ip,
                int page,
                int size) {
}
