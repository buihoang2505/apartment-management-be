package com.apartment.app.zone.dto;

import com.apartment.domain.zone.Zone;

import java.time.LocalDateTime;
import java.util.UUID;

public record ZoneResponse(
        UUID id,
        String name,
        String code,
        String description,
        LocalDateTime createdAt
) {
    public static ZoneResponse from(Zone zone) {
        return new ZoneResponse(
                zone.getId(),
                zone.getName(),
                zone.getCode(),
                zone.getDescription(),
                zone.getCreatedAt()
        );
    }
}