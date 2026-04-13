package com.apartment.app.zone.dto;

import com.apartment.domain.zone.Building;

import java.time.LocalDateTime;
import java.util.UUID;

public record BuildingResponse(
        UUID id,
        UUID zoneId,
        String zoneName,
        String name,
        String code,
        LocalDateTime createdAt
) {
    public static BuildingResponse from(Building building) {
        return new BuildingResponse(
                building.getId(),
                building.getZone().getId(),
                building.getZone().getName(),
                building.getName(),
                building.getCode(),
                building.getCreatedAt()
        );
    }
}