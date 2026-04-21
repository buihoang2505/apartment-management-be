package com.apartment.app.audit.query;

import com.apartment.domain.audit.AuditLog;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuditLogSpecification {

    private AuditLogSpecification() {}

    public static Specification<AuditLog> withFilters(
            UUID userId,
            String action,
            String entityType,
            String fromDate,
            String toDate,
            String ip) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }

            if (action != null && !action.isBlank()) {
                predicates.add(cb.like(cb.upper(root.get("action")),
                        "%" + action.toUpperCase() + "%"));
            }

            if (entityType != null && !entityType.isBlank()) {
                predicates.add(cb.like(cb.upper(root.get("entityType")),
                        "%" + entityType.toUpperCase() + "%"));
            }

            if (fromDate != null && !fromDate.isBlank()) {
                LocalDateTime from = LocalDate.parse(fromDate).atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), from));
            }

            if (toDate != null && !toDate.isBlank()) {
                LocalDateTime to = LocalDate.parse(toDate).plusDays(1).atStartOfDay();
                predicates.add(cb.lessThan(root.get("createdAt"), to));
            }

            if (ip != null && !ip.isBlank()) {
                predicates.add(cb.like(root.get("ipAddress"), "%" + ip + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
