package com.apartment.app.apartment.dto;

import com.apartment.domain.apartment.Apartment;
import com.apartment.domain.apartment.ApartmentImage;
import com.apartment.domain.apartment.ApartmentStatus;
import com.apartment.domain.apartment.ApartmentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ApartmentResponse(
        UUID id,
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
        UUID buildingId,
        String buildingName,
        String buildingCode,
        UUID zoneId,
        String zoneName,
        List<ImageResponse> images,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record ImageResponse(UUID id, String url, String label, int sortOrder) {
        public static ImageResponse from(ApartmentImage img) {
            return new ImageResponse(img.getId(), img.getUrl(), img.getLabel(), img.getSortOrder());
        }
    }

    public static ApartmentResponse from(Apartment a) {
        return new ApartmentResponse(
                a.getId(),
                a.getUnitCode(),
                a.getDisplayCode(),
                a.getArea(),
                a.getSellingPrice(),
                a.getTax(),
                a.getStatus(),
                a.getFurnitureDescription(),
                a.getApartmentType(),
                a.getFloorNumber(),
                a.getDirection(),
                a.getBedroomCount(),
                a.getBuilding().getId(),
                a.getBuilding().getName(),
                a.getBuilding().getCode(),
                a.getBuilding().getZone().getId(),
                a.getBuilding().getZone().getName(),
                a.getImages().stream().map(ImageResponse::from).toList(),
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }
}