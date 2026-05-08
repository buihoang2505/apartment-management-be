package com.apartment.interfaces.contract.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AddPaymentScheduleRequest(
        @NotNull LocalDate dueDate,
        @NotNull @Positive BigDecimal amount,
        @Size(max = 500) String note
) {}
