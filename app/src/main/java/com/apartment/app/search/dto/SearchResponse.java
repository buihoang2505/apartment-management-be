package com.apartment.app.search.dto;

import java.util.List;

public record SearchResponse(
        List<ApartmentSearchItem> apartments,
        List<EmployeeSearchItem> employees,
        List<PortfolioSearchItem> portfolios
) {
    public record ApartmentSearchItem(
            String id,
            String unitCode,
            String displayCode,
            String status,
            String apartmentType
    ) {}

    public record EmployeeSearchItem(
            String id,
            String fullName,
            String email,
            String position,
            String department
    ) {}

    public record PortfolioSearchItem(
            String id,
            String name,
            String description,
            int zoneCount
    ) {}
}
