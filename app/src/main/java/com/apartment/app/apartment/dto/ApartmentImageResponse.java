package com.apartment.app.apartment.dto;

import com.apartment.domain.apartment.ApartmentImage;

import java.util.UUID;

public record ApartmentImageResponse(UUID id, String url, String label, int sortOrder) {
    public static ApartmentImageResponse from(ApartmentImage image) {
        return new ApartmentImageResponse(
                image.getId(),
                image.getUrl(),
                image.getLabel(),
                image.getSortOrder()
        );
    }
}
