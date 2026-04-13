package com.apartment.app.dashboard;

import com.apartment.app.dashboard.dto.DashboardStats;
import com.apartment.domain.apartment.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final ApartmentRepository apartmentRepository;

    public DashboardStats getStats() {
        long total = apartmentRepository.count();

        Map<String, Long> byStatus = new HashMap<>();
        for (Object[] row : apartmentRepository.countByStatus()) {
            byStatus.put(row[0].toString(), (Long) row[1]);
        }

        YearMonth current = YearMonth.now();
        YearMonth previous = current.minusMonths(1);

        long thisMonth = apartmentRepository.countCreatedBetween(
                current.atDay(1).atStartOfDay(),
                current.atEndOfMonth().atTime(23, 59, 59)
        );
        long lastMonth = apartmentRepository.countCreatedBetween(
                previous.atDay(1).atStartOfDay(),
                previous.atEndOfMonth().atTime(23, 59, 59)
        );

        double percentage = lastMonth == 0
                ? (thisMonth > 0 ? 100.0 : 0.0)
                : ((double) (thisMonth - lastMonth) / lastMonth) * 100;

        return new DashboardStats(total, byStatus,
                new DashboardStats.GrowthStats(thisMonth, lastMonth, Math.round(percentage * 10.0) / 10.0));
    }
}