package com.apartment.app.zone.command;

import com.apartment.domain.zone.BuildingType;

import java.util.UUID;

public record CreateBuildingCommand(
        UUID zoneId,
        String name,
        String code,
        BuildingType type,
        Integer totalFloors,
        String description
) {}