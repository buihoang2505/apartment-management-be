package com.apartment.interfaces.dashboard;

import com.apartment.app.dashboard.dto.DashboardStatsResponse;
import com.apartment.app.dashboard.dto.GrowthTrend;

import java.util.LinkedHashMap;
import java.util.Map;

public record DashboardStatsView(
        long total,
        Map<String, Long> byStatus,
        Map<String, Long> byType,
        GrowthView growth
) {
    public record GrowthView(long thisMonth, long lastMonth, Double percentage, String label) {}

    public static DashboardStatsView from(DashboardStatsResponse r) {
        Map<String, Long> byStatus = new LinkedHashMap<>();
        r.byStatus().forEach((k, v) -> byStatus.put(k.name(), v));

        Map<String, Long> byType = new LinkedHashMap<>();
        r.byType().forEach((k, v) -> byType.put(k.name(), v));

        var g = r.growth();
        return new DashboardStatsView(
                r.total(), byStatus, byType,
                new GrowthView(g.thisMonth(), g.lastMonth(), g.percentage(), labelOf(g.trend()))
        );
    }

    private static String labelOf(GrowthTrend trend) {
        return switch (trend) {
            case UP          -> "Tăng so với tháng trước";
            case DOWN        -> "Giảm so với tháng trước";
            case FLAT        -> "Không thay đổi so với tháng trước";
            case NO_PREVIOUS -> "Chưa có dữ liệu tháng trước";
            case NO_DATA     -> "Chưa có dữ liệu";
        };
    }
}
