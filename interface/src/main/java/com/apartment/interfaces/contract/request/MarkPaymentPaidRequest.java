package com.apartment.interfaces.contract.request;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MarkPaymentPaidRequest(
        @PositiveOrZero BigDecimal paidAmount,
        LocalDate paidDate,
        @Size(max = 500) String note
) {}
