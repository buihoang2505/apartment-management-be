package com.apartment.interfaces.audit;

import com.apartment.app.audit.dto.AuditLogResponse;
import com.apartment.app.audit.handler.AuditLogQueryHandler;
import com.apartment.interfaces.shared.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Audit Logs", description = "Lịch sử thao tác hệ thống")
@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogQueryHandler auditLogQueryHandler;

    @Operation(summary = "Danh sách audit logs — ADMIN xem tất cả, MANAGER chỉ xem của mình")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<CommonResponse<Page<AuditLogResponse>>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size,
            Authentication auth) {
        String role = extractRole(auth);
        return ResponseEntity.ok(CommonResponse.ok(
                auditLogQueryHandler.findAll(page, size, auth.getName(), role)));
    }

    @Operation(summary = "Lọc audit logs theo entity — chỉ ADMIN")
    @GetMapping("/by-entity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<List<AuditLogResponse>>> getByEntity(
            @RequestParam("entityType") String entityType,
            @RequestParam("entityId") String entityId) {
        return ResponseEntity.ok(CommonResponse.ok(
                auditLogQueryHandler.findByEntity(entityType, entityId)));
    }

    @Operation(summary = "Lọc audit logs theo user — MANAGER chỉ xem được của chính mình")
    @GetMapping("/by-user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<CommonResponse<List<AuditLogResponse>>> getByUser(
            @PathVariable("userId") UUID userId,
            Authentication auth) {
        String role = extractRole(auth);
        return ResponseEntity.ok(CommonResponse.ok(
                auditLogQueryHandler.findByUser(userId, auth.getName(), role)));
    }

    private String extractRole(Authentication auth) {
        return auth.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("");
    }
}
