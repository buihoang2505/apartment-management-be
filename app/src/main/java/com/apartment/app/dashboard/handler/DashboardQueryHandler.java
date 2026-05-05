package com.apartment.app.dashboard.handler;

import com.apartment.app.dashboard.dto.DashboardStatsResponse;
import com.apartment.app.dashboard.dto.GrowthTrend;
import com.apartment.domain.apartment.ApartmentStatus;
import com.apartment.domain.apartment.ApartmentType;
import com.apartment.domain.dashboard.DashboardReadRepository;
import com.apartment.domain.dashboard.DashboardStatsProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardQueryHandler {

    private final DashboardReadRepository dashboardReadRepository;

    @Cacheable(value = "dashboard.stats", key = "#zoneId == null ? 'ALL' : #zoneId.toString()")
    public DashboardStatsResponse getStats(UUID zoneId) {
        var current = MonthRange.of(YearMonth.now());
        var previous = MonthRange.of(YearMonth.now().minusMonths(1));

        DashboardStatsProjection p = dashboardReadRepository.aggregate(
                zoneId,
                current.from(), current.toExclusive(),
                previous.from(), previous.toExclusive()
        );

        Map<ApartmentStatus, Long> byStatus = new EnumMap<>(ApartmentStatus.class);
        byStatus.put(ApartmentStatus.DANG_BAN, p.getDangBan());
        byStatus.put(ApartmentStatus.DA_COC, p.getDaCoc());
        byStatus.put(ApartmentStatus.DA_BAN, p.getDaBan());
        byStatus.put(ApartmentStatus.GIU_CHO, p.getGiuCho());
        byStatus.put(ApartmentStatus.KHOA, p.getKhoa());

        Map<ApartmentType, Long> byType = new EnumMap<>(ApartmentType.class);
        byType.put(ApartmentType.STUDIO, p.getStudio());
        byType.put(ApartmentType.ONE_BEDROOM, p.getOneBedroom());
        byType.put(ApartmentType.TWO_BEDROOM, p.getTwoBedroom());
        byType.put(ApartmentType.TWO_BEDROOM_PLUS, p.getTwoBedroomPlus());
        byType.put(ApartmentType.THREE_BEDROOM, p.getThreeBedroom());
        byType.put(ApartmentType.THREE_BEDROOM_PLUS, p.getThreeBedroomPlus());
        byType.put(ApartmentType.PENTHOUSE, p.getPenthouse());
        byType.put(ApartmentType.DUPLEX, p.getDuplex());
        byType.put(ApartmentType.OTHER, p.getOther());

        var growth = computeGrowth(p.getThisMonth(), p.getLastMonth());

        return new DashboardStatsResponse(p.getTotal(), byStatus, byType, growth);
    }

    private DashboardStatsResponse.GrowthStats computeGrowth(long thisMonth, long lastMonth) {
        if (lastMonth == 0 && thisMonth == 0) {
            return new DashboardStatsResponse.GrowthStats(thisMonth, lastMonth, null, GrowthTrend.NO_DATA);
        }
        if (lastMonth == 0) {
            return new DashboardStatsResponse.GrowthStats(thisMonth, lastMonth, null, GrowthTrend.NO_PREVIOUS);
        }
        double percentage = Math.round(((double) (thisMonth - lastMonth) / lastMonth) * 1000.0) / 10.0;
        GrowthTrend trend = percentage > 0 ? GrowthTrend.UP
                          : percentage < 0 ? GrowthTrend.DOWN
                          : GrowthTrend.FLAT;
        return new DashboardStatsResponse.GrowthStats(thisMonth, lastMonth, percentage, trend);
    }
}
