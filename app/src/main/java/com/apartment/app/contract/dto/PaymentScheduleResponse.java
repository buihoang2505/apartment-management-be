package com.apartment.app.contract.dto;

import com.apartment.domain.contract.PaymentSchedule;
import com.apartment.domain.contract.PaymentScheduleCategory;
import com.apartment.domain.contract.PaymentScheduleStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PaymentScheduleResponse(
        UUID id,
        PaymentScheduleCategory category,
        int periodIndex,
        LocalDate dueDate,
        BigDecimal amount,
        BigDecimal paidAmount,
        LocalDate paidDate,
        PaymentScheduleStatus status,
        String note
) {
    public static PaymentScheduleResponse from(PaymentSchedule s) {
        return new PaymentScheduleResponse(
                s.getId(),
                s.getCategory(),
                s.getPeriodIndex(),
                s.getDueDate(),
                s.getAmount(),
                s.getPaidAmount(),
                s.getPaidDate(),
                s.getStatus(),
                s.getNote()
        );
    }
}
