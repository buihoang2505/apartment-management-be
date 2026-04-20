package com.apartment.interfaces.audit;

import com.apartment.app.audit.dto.AuditLogResponse;
import com.apartment.app.audit.handler.AuditLogQueryHandler;
import com.apartment.interfaces.shared.response.CommonResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Audit Logs", description = "Lịch sử thao tác hệ thống")
@RestController
@RequestMapping("/admin/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogQueryHandler auditLogQueryHandler;

    @GetMapping
    public ResponseEntity<CommonResponse<Page<AuditLogResponse>>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size) {
        return ResponseEntity.ok(CommonResponse.ok(auditLogQueryHandler.findAll(page, size)));
    }

    @GetMapping("/by-entity")
    public ResponseEntity<CommonResponse<List<AuditLogResponse>>> getByEntity(
            @RequestParam("entityType") String entityType,
            @RequestParam("entityId") String entityId) {
        return ResponseEntity.ok(CommonResponse.ok(auditLogQueryHandler.findByEntity(entityType, entityId)));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<CommonResponse<List<AuditLogResponse>>> getByUser(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(CommonResponse.ok(auditLogQueryHandler.findByUser(userId)));
    }
}