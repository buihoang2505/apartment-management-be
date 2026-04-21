package com.apartment.app.dashboard.dto;

import java.util.Map;

public record DashboardStatsResponse(
        long total,
        Map<String, Long> byStatus,
        Map<String, Long> byType,
        GrowthStats growth
) {
    public record GrowthStats(long thisMonth, long lastMonth, Double percentage, String label) {}
}