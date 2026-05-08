package com.apartment.interfaces.contract.request;

import com.apartment.domain.contract.ContractType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateContractRequest(
        @NotBlank @Size(max = 50) String code,
        @NotNull ContractType type,
        @NotNull UUID apartmentId,
        @NotNull UUID customerId,
        @NotNull LocalDate startDate,
        LocalDate endDate,
        LocalDate signedDate,
        @NotNull @Positive BigDecimal totalAmount,
        BigDecimal monthlyAmount,
        BigDecimal depositAmount,
        @Min(1) Integer numberOfInstallments,
        @Size(max = 2000) String note,
        Boolean autoGenerateSchedule,
        Boolean generateDepositRefund
) {}
