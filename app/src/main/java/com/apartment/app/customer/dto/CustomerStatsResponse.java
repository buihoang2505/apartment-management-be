package com.apartment.app.customer.dto;

import com.apartment.domain.customer.CustomerStatus;

import java.util.Map;

public record CustomerStatsResponse(
        long total,
        Map<CustomerStatus, Long> byStatus
) {}
