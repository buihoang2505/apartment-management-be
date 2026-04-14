package com.apartment.app.zone.command;

import com.apartment.domain.zone.BuildingType;

import java.util.UUID;

public record UpdateBuildingCommand(
        UUID buildingId,
        String name,
        String code,
        BuildingType type,
        Integer totalFloors,
        String description
) {}