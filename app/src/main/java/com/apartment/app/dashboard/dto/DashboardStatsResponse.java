package com.apartment.app.dashboard.dto;

import com.apartment.domain.apartment.ApartmentStatus;
import com.apartment.domain.apartment.ApartmentType;

import java.util.Map;

public record DashboardStatsResponse(
        long total,
        Map<ApartmentStatus, Long> byStatus,
        Map<ApartmentType, Long> byType,
        GrowthStats growth
) {
    public record GrowthStats(long thisMonth, long lastMonth, Double percentage, GrowthTrend trend) {}
}
