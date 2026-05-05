package com.apartment.app.dashboard.handler;

import java.time.LocalDateTime;
import java.time.YearMonth;

record MonthRange(LocalDateTime from, LocalDateTime toExclusive) {

    static MonthRange of(YearMonth ym) {
        return new MonthRange(
                ym.atDay(1).atStartOfDay(),
                ym.plusMonths(1).atDay(1).atStartOfDay()
        );
    }
}
