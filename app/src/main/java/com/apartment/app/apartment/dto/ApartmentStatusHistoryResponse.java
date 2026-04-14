package com.apartment.app.apartment.dto;

import com.apartment.domain.apartment.ApartmentStatus;
import com.apartment.domain.apartment.ApartmentStatusHistory;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApartmentStatusHistoryResponse(
        UUID id,
        ApartmentStatus oldStatus,
        ApartmentStatus newStatus,
        String changedBy,
        String note,
        LocalDateTime createdAt
) {
    public static ApartmentStatusHistoryResponse from(ApartmentStatusHistory h) {
        return new ApartmentStatusHistoryResponse(
                h.getId(),
                h.getOldStatus(),
                h.getNewStatus(),
                h.getChangedBy() != null ? h.getChangedBy().getUsername() : null,
                h.getNote(),
                h.getCreatedAt()
        );
    }
}