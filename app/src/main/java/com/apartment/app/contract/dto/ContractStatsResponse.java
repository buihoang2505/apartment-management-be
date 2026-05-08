package com.apartment.app.contract.dto;

import com.apartment.domain.contract.ContractStatus;

import java.math.BigDecimal;
import java.util.Map;

public record ContractStatsResponse(
        long total,
        Map<ContractStatus, Long> byStatus,
        BigDecimal outstandingAmount,
        BigDecimal overdueAmount
) {}
