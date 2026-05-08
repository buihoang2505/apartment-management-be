package com.apartment.app.contract.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MarkPaymentPaidCommand(
        UUID scheduleId,
        BigDecimal paidAmount,
        LocalDate paidDate,
        String note
) {}
