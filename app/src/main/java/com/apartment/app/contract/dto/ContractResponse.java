package com.apartment.app.contract.dto;

import com.apartment.domain.contract.Contract;
import com.apartment.domain.contract.ContractStatus;
import com.apartment.domain.contract.ContractType;
import com.apartment.domain.contract.PaymentScheduleCategory;
import com.apartment.domain.contract.PaymentScheduleStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ContractResponse(
        UUID id,
        String code,
        ContractType type,
        ContractStatus status,
        UUID apartmentId,
        String apartmentCode,
        UUID customerId,
        String customerName,
        String customerPhone,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate signedDate,
        BigDecimal totalAmount,
        BigDecimal monthlyAmount,
        BigDecimal depositAmount,
        BigDecimal paidAmount,
        BigDecimal outstandingAmount,
        String note,
        List<PaymentScheduleResponse> schedules
) {
    public static ContractResponse from(Contract c, boolean includeSchedules) {
        BigDecimal paid = BigDecimal.ZERO;
        BigDecimal outstanding = BigDecimal.ZERO;
        List<PaymentScheduleResponse> sch = List.of();

        if (c.getSchedules() != null) {
            for (var s : c.getSchedules()) {
                // Chỉ tính tiến độ thanh toán hợp đồng từ kỳ INSTALLMENT;
                // DEPOSIT / DEPOSIT_REFUND là dòng tiền cọc, không thuộc giá trị hợp đồng.
                if (s.getCategory() != PaymentScheduleCategory.INSTALLMENT) continue;
                BigDecimal already = s.getPaidAmount() != null ? s.getPaidAmount() : BigDecimal.ZERO;
                paid = paid.add(already);
                if (s.getStatus() != PaymentScheduleStatus.PAID) {
                    outstanding = outstanding.add(s.getAmount().subtract(already));
                }
            }
            if (includeSchedules) {
                sch = c.getSchedules().stream().map(PaymentScheduleResponse::from).toList();
            }
        }

        return new ContractResponse(
                c.getId(),
                c.getCode(),
                c.getType(),
                c.getStatus(),
                c.getApartment() != null ? c.getApartment().getId() : null,
                c.getApartment() != null ? c.getApartment().getUnitCode() : null,
                c.getCustomer() != null ? c.getCustomer().getId() : null,
                c.getCustomer() != null ? c.getCustomer().getFullName() : null,
                c.getCustomer() != null ? c.getCustomer().getPhone() : null,
                c.getStartDate(),
                c.getEndDate(),
                c.getSignedDate(),
                c.getTotalAmount(),
                c.getMonthlyAmount(),
                c.getDepositAmount(),
                paid,
                outstanding,
                c.getNote(),
                sch
        );
    }
}
