package com.apartment.app.apartment.command;

import com.apartment.domain.apartment.ApartmentStatus;
import com.apartment.domain.apartment.ApartmentType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateApartmentCommand(
        String unitCode,
        String displayCode,
        BigDecimal area,
        BigDecimal sellingPrice,
        BigDecimal tax,
        ApartmentStatus status,
        String furnitureDescription,
        ApartmentType apartmentType,
        Integer floorNumber,
        String direction,
        Integer bedroomCount,
        String statusNote,
        UUID buildingId,
        List<ImageCommand> images
) {
    public record ImageCommand(String url, String label, int sortOrder) {}
}