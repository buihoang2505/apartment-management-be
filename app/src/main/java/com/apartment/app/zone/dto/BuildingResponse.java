package com.apartment.app.zone.dto;

import com.apartment.domain.zone.Building;
import com.apartment.domain.zone.BuildingType;

import java.time.LocalDateTime;
import java.util.UUID;

public record BuildingResponse(
        UUID id,
        UUID zoneId,
        String zoneName,
        String name,
        String code,
        BuildingType type,
        Integer totalFloors,
        String description,
        LocalDateTime createdAt
) {
    public static BuildingResponse from(Building building) {
        return new BuildingResponse(
                building.getId(),
                building.getZone().getId(),
                building.getZone().getName(),
                building.getName(),
                building.getCode(),
                building.getType(),
                building.getTotalFloors(),
                building.getDescription(),
                building.getCreatedAt()
        );
    }
}