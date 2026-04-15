package com.apartment.app.dashboard.handler;

import com.apartment.app.dashboard.dto.DashboardStatsResponse;
import com.apartment.domain.apartment.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardQueryHandler {

    private final ApartmentRepository apartmentRepository;

    public DashboardStatsResponse getStats(UUID zoneId) {
        boolean filtered = zoneId != null;

        long total = filtered
                ? apartmentRepository.countByZoneId(zoneId)
                : apartmentRepository.count();

        Map<String, Long> byStatus = new HashMap<>();
        for (Object[] row : filtered
                ? apartmentRepository.countByStatusAndZoneId(zoneId)
                : apartmentRepository.countByStatus()) {
            byStatus.put(row[0].toString(), (Long) row[1]);
        }

        Map<String, Long> byType = new HashMap<>();
        for (Object[] row : filtered
                ? apartmentRepository.countByTypeAndZoneId(zoneId)
                : apartmentRepository.countByType()) {
            byType.put(row[0].toString(), (Long) row[1]);
        }

        YearMonth current = YearMonth.now();
        YearMonth previous = current.minusMonths(1);

        long thisMonth = filtered
                ? apartmentRepository.countByZoneIdAndCreatedBetween(zoneId,
                        current.atDay(1).atStartOfDay(),
                        current.atEndOfMonth().atTime(23, 59, 59))
                : apartmentRepository.countCreatedBetween(
                        current.atDay(1).atStartOfDay(),
                        current.atEndOfMonth().atTime(23, 59, 59));
        long lastMonth = filtered
                ? apartmentRepository.countByZoneIdAndCreatedBetween(zoneId,
                        previous.atDay(1).atStartOfDay(),
                        previous.atEndOfMonth().atTime(23, 59, 59))
                : apartmentRepository.countCreatedBetween(
                        previous.atDay(1).atStartOfDay(),
                        previous.atEndOfMonth().atTime(23, 59, 59));

        double percentage = lastMonth == 0
                ? (thisMonth > 0 ? 100.0 : 0.0)
                : ((double) (thisMonth - lastMonth) / lastMonth) * 100;

        return new DashboardStatsResponse(total, byStatus, byType,
                new DashboardStatsResponse.GrowthStats(thisMonth, lastMonth,
                        Math.round(percentage * 10.0) / 10.0));
    }
}