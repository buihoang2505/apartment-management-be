package com.apartment.app.portfolio.dto;

import com.apartment.app.zone.dto.ZoneResponse;
import com.apartment.domain.portfolio.Portfolio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PortfolioResponse(
        UUID id,
        String name,
        String description,
        List<ZoneResponse> zones,
        LocalDateTime createdAt
) {
    public static PortfolioResponse from(Portfolio p) {
        return new PortfolioResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getZones().stream().map(ZoneResponse::from).toList(),
                p.getCreatedAt()
        );
    }
}