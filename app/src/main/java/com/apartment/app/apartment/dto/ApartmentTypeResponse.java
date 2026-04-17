package com.apartment.app.apartment.dto;

import com.apartment.domain.apartment.ApartmentType;

public record ApartmentTypeResponse(String code, String label) {

    public static ApartmentTypeResponse from(ApartmentType type) {
        return new ApartmentTypeResponse(type.name(), type.getLabel());
    }
}
