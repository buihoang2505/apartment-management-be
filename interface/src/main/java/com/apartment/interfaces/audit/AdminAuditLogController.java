package com.apartment.interfaces.audit;

import com.apartment.app.audit.dto.AuditLogResponse;
import com.apartment.app.audit.handler.AuditLogQueryHandler;
import com.apartment.app.audit.query.AuditLogQuery;
import com.apartment.app.audit.query.AuditLogSpecification;
import com.apartment.interfaces.shared.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Admin – Audit Logs", description = "Quản trị lịch sử thao tác hệ thống")
@RestController
@RequestMapping("/admin/audit-logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAuditLogController {

    private final AuditLogQueryHandler auditLogQueryHandler;

    @Operation(summary = "Danh sách audit logs có lọc nâng cao — chỉ ADMIN")
    @GetMapping
    public ResponseEntity<CommonResponse<Page<AuditLogResponse>>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "userId", required = false) UUID userId,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "entityType", required = false) String entityType,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "ip", required = false) String ip) {

        AuditLogQuery query = new AuditLogQuery(userId, action, entityType, fromDate, toDate, ip, page, size);
        var spec = AuditLogSpecification.withFilters(userId, action, entityType, fromDate, toDate, ip);

        return ResponseEntity.ok(CommonResponse.ok(auditLogQueryHandler.query(query, spec)));
    }
}
