package com.apartment.app.contract.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record AddPaymentScheduleCommand(
        UUID contractId,
        LocalDate dueDate,
        BigDecimal amount,
        String note
) {}
