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

        Double percentage;
        String label;
        if (lastMonth == 0) {
            percentage = null;
            label = "Chưa có dữ liệu tháng trước";
        } else {
            percentage = Math.round(((double) (thisMonth - lastMonth) / lastMonth) * 100 * 10.0) / 10.0;
            label = percentage >= 0 ? "Tăng so với tháng trước" : "Giảm so với tháng trước";
        }

        return new DashboardStatsResponse(total, byStatus, byType,
                new DashboardStatsResponse.GrowthStats(thisMonth, lastMonth, percentage, label));
    }
}