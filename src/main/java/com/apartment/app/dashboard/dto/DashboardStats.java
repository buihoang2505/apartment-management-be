package com.apartment.app.dashboard.dto;

import java.util.Map;

public record DashboardStats(
        long total,
        Map<String, Long> byStatus,
        GrowthStats growth
) {
    public record GrowthStats(long thisMonth, long lastMonth, double percentage) {}
}