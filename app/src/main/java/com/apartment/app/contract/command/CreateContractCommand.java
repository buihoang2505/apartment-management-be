package com.apartment.app.contract.command;

import com.apartment.domain.contract.ContractType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateContractCommand(
        String code,
        ContractType type,
        UUID apartmentId,
        UUID customerId,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate signedDate,
        BigDecimal totalAmount,
        BigDecimal monthlyAmount,
        BigDecimal depositAmount,
        Integer numberOfInstallments,
        String note,
        boolean autoGenerateSchedule,
        boolean generateDepositRefund
) {}
