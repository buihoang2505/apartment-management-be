package com.apartment.app.contract.handler;

import com.apartment.app.apartment.exception.ApartmentNotFoundException;
import com.apartment.app.contract.command.*;
import com.apartment.app.contract.dto.ContractResponse;
import com.apartment.app.contract.dto.PaymentScheduleResponse;
import com.apartment.app.contract.exception.ContractNotFoundException;
import com.apartment.app.contract.exception.PaymentScheduleNotFoundException;
import com.apartment.app.customer.exception.CustomerNotFoundException;
import com.apartment.app.shared.port.NotificationPort;
import com.apartment.domain.apartment.ApartmentRepository;
import com.apartment.domain.contract.*;
import com.apartment.domain.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractCommandHandler {

    private final ContractRepository contractRepository;
    private final PaymentScheduleRepository paymentScheduleRepository;
    private final ApartmentRepository apartmentRepository;
    private final CustomerRepository customerRepository;
    private final NotificationPort notificationPort;

    @Transactional
    public ContractResponse handle(CreateContractCommand cmd) {
        if (contractRepository.existsByCode(cmd.code())) {
            throw new IllegalArgumentException("Mã hợp đồng đã tồn tại: " + cmd.code());
        }
        if (cmd.endDate() != null && cmd.startDate() != null
                && cmd.endDate().isBefore(cmd.startDate())) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
        var apartment = apartmentRepository.findById(cmd.apartmentId())
                .orElseThrow(() -> new ApartmentNotFoundException(cmd.apartmentId()));
        var customer = customerRepository.findById(cmd.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(cmd.customerId()));

        var contract = Contract.builder()
                .code(cmd.code())
                .type(cmd.type())
                .apartment(apartment)
                .customer(customer)
                .startDate(cmd.startDate())
                .endDate(cmd.endDate())
                .signedDate(cmd.signedDate())
                .totalAmount(cmd.totalAmount())
                .monthlyAmount(cmd.monthlyAmount())
                .depositAmount(cmd.depositAmount())
                .status(ContractStatus.DRAFT)
                .note(cmd.note())
                .build();

        contract = contractRepository.save(contract);

        if (cmd.autoGenerateSchedule()) {
            generateSchedules(contract, cmd);
        }

        notificationPort.push(
                "Hợp đồng mới",
                "Hợp đồng " + contract.getCode() + " đã được tạo",
                "CONTRACT",
                contract.getId().toString());

        return ContractResponse.from(contract, true);
    }

    private void generateSchedules(Contract contract, CreateContractCommand cmd) {
        List<PaymentSchedule> schedules = new ArrayList<>();

        // 1) Kỳ thu cọc — luôn sinh nếu depositAmount > 0
        BigDecimal deposit = contract.getDepositAmount();
        if (deposit != null && deposit.signum() > 0) {
            LocalDate depositDue = contract.getSignedDate() != null
                    ? contract.getSignedDate()
                    : contract.getStartDate();
            schedules.add(PaymentSchedule.builder()
                    .contract(contract)
                    .category(PaymentScheduleCategory.DEPOSIT)
                    .periodIndex(0)
                    .dueDate(depositDue)
                    .amount(deposit)
                    .status(PaymentScheduleStatus.PENDING)
                    .note("Tiền cọc")
                    .build());
        }

        // 2) Các kỳ thanh toán chính (RENT theo tháng / SALE trả góp)
        schedules.addAll(buildInstallments(contract, cmd));

        // 3) Kỳ hoàn cọc (tuỳ chọn) — vào ngày kết thúc hợp đồng
        if (cmd.generateDepositRefund() && deposit != null && deposit.signum() > 0
                && contract.getEndDate() != null) {
            schedules.add(PaymentSchedule.builder()
                    .contract(contract)
                    .category(PaymentScheduleCategory.DEPOSIT_REFUND)
                    .periodIndex(0)
                    .dueDate(contract.getEndDate())
                    .amount(deposit)
                    .status(PaymentScheduleStatus.PENDING)
                    .note("Hoàn cọc")
                    .build());
        }

        contract.getSchedules().addAll(schedules);
    }

    private List<PaymentSchedule> buildInstallments(Contract contract, CreateContractCommand cmd) {
        List<PaymentSchedule> result = new ArrayList<>();
        ContractType type = contract.getType();

        if (type == ContractType.RENT) {
            if (contract.getStartDate() == null || contract.getEndDate() == null
                    || contract.getMonthlyAmount() == null) {
                throw new IllegalArgumentException(
                        "Hợp đồng RENT cần startDate, endDate và monthlyAmount để sinh kỳ");
            }
            // Đếm kỳ đầy đủ; 2026-01-15 → 2026-12-14 = 11 kỳ.
            long months = ChronoUnit.MONTHS.between(contract.getStartDate(), contract.getEndDate()) + 1;
            if (months <= 0) {
                throw new IllegalArgumentException("Khoảng thời gian hợp đồng không hợp lệ");
            }
            assertTotalMatches(contract.getTotalAmount(), contract.getMonthlyAmount(), months);
            for (int i = 0; i < months; i++) {
                result.add(PaymentSchedule.builder()
                        .contract(contract)
                        .category(PaymentScheduleCategory.INSTALLMENT)
                        .periodIndex(i + 1)
                        .dueDate(contract.getStartDate().plusMonths(i))
                        .amount(contract.getMonthlyAmount())
                        .status(PaymentScheduleStatus.PENDING)
                        .build());
            }
            return result;
        }

        // SALE
        int n = cmd.numberOfInstallments() != null ? cmd.numberOfInstallments() : 1;
        if (n < 1) throw new IllegalArgumentException("Số kỳ trả góp phải >= 1");
        if (n == 1) {
            result.add(PaymentSchedule.builder()
                    .contract(contract)
                    .category(PaymentScheduleCategory.INSTALLMENT)
                    .periodIndex(1)
                    .dueDate(contract.getStartDate())
                    .amount(contract.getTotalAmount())
                    .status(PaymentScheduleStatus.PENDING)
                    .build());
            return result;
        }
        // Trả góp nhiều kỳ — yêu cầu monthlyAmount và khớp tổng
        if (contract.getMonthlyAmount() == null) {
            throw new IllegalArgumentException(
                    "Hợp đồng SALE trả góp cần monthlyAmount cho mỗi kỳ");
        }
        assertTotalMatches(contract.getTotalAmount(), contract.getMonthlyAmount(), n);
        for (int i = 0; i < n; i++) {
            result.add(PaymentSchedule.builder()
                    .contract(contract)
                    .category(PaymentScheduleCategory.INSTALLMENT)
                    .periodIndex(i + 1)
                    .dueDate(contract.getStartDate().plusMonths(i))
                    .amount(contract.getMonthlyAmount())
                    .status(PaymentScheduleStatus.PENDING)
                    .build());
        }
        return result;
    }

    private void assertTotalMatches(BigDecimal total, BigDecimal perPeriod, long periods) {
        BigDecimal expected = perPeriod.multiply(BigDecimal.valueOf(periods));
        if (total.compareTo(expected) != 0) {
            throw new IllegalArgumentException(
                    "Tổng giá trị (" + total + ") không khớp với " + periods + " kỳ × "
                            + perPeriod + " = " + expected);
        }
    }

    @Transactional
    public ContractResponse handle(UpdateContractStatusCommand cmd) {
        var contract = contractRepository.findById(cmd.id())
                .orElseThrow(() -> new ContractNotFoundException(cmd.id()));
        assertValidTransition(contract.getStatus(), cmd.status());
        contract.setStatus(cmd.status());
        return ContractResponse.from(contractRepository.save(contract), true);
    }

    private void assertValidTransition(ContractStatus from, ContractStatus to) {
        if (from == to) return;
        boolean ok = switch (from) {
            case DRAFT -> to == ContractStatus.ACTIVE || to == ContractStatus.CANCELLED;
            case ACTIVE -> to == ContractStatus.COMPLETED || to == ContractStatus.CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
        if (!ok) {
            throw new IllegalStateException(
                    "Không thể chuyển trạng thái hợp đồng từ " + from + " sang " + to);
        }
    }

    @Transactional
    public void handle(DeleteContractCommand cmd) {
        var contract = contractRepository.findById(cmd.id())
                .orElseThrow(() -> new ContractNotFoundException(cmd.id()));
        if (contract.getStatus() == ContractStatus.ACTIVE || contract.getStatus() == ContractStatus.COMPLETED) {
            throw new IllegalStateException("Không thể xoá hợp đồng đã kích hoạt hoặc đã hoàn tất");
        }
        contractRepository.deleteById(cmd.id());
    }

    @Transactional
    public PaymentScheduleResponse handle(AddPaymentScheduleCommand cmd) {
        var contract = contractRepository.findWithSchedulesById(cmd.contractId())
                .orElseThrow(() -> new ContractNotFoundException(cmd.contractId()));
        int nextIndex = contract.getSchedules().stream()
                .mapToInt(PaymentSchedule::getPeriodIndex).max().orElse(0) + 1;

        var schedule = PaymentSchedule.builder()
                .contract(contract)
                .periodIndex(nextIndex)
                .dueDate(cmd.dueDate())
                .amount(cmd.amount())
                .status(PaymentScheduleStatus.PENDING)
                .note(cmd.note())
                .build();
        return PaymentScheduleResponse.from(paymentScheduleRepository.save(schedule));
    }

    @Transactional
    public PaymentScheduleResponse handle(MarkPaymentPaidCommand cmd) {
        var schedule = paymentScheduleRepository.findById(cmd.scheduleId())
                .orElseThrow(() -> new PaymentScheduleNotFoundException(cmd.scheduleId()));

        BigDecimal paid = cmd.paidAmount() != null ? cmd.paidAmount() : schedule.getAmount();
        if (paid.signum() <= 0) {
            throw new IllegalArgumentException("Số tiền thanh toán phải lớn hơn 0");
        }
        if (schedule.getAmount() != null && paid.compareTo(schedule.getAmount()) > 0) {
            throw new IllegalArgumentException(
                    "Số tiền thanh toán vượt quá số tiền của kỳ (" + schedule.getAmount() + ")");
        }
        schedule.setPaidAmount(paid);
        schedule.setPaidDate(cmd.paidDate() != null ? cmd.paidDate() : LocalDate.now());
        schedule.setStatus(
                schedule.getAmount() != null && paid.compareTo(schedule.getAmount()) < 0
                        ? PaymentScheduleStatus.PARTIAL
                        : PaymentScheduleStatus.PAID);
        if (cmd.note() != null) schedule.setNote(cmd.note());

        var saved = paymentScheduleRepository.save(schedule);

        // auto-complete contract khi tất cả kỳ INSTALLMENT đã PAID
        // (DEPOSIT / DEPOSIT_REFUND tracked riêng, không quyết định trạng thái hợp đồng)
        var contract = contractRepository.findWithSchedulesById(saved.getContract().getId()).orElse(null);
        var installments = contract == null ? List.<PaymentSchedule>of()
                : contract.getSchedules().stream()
                        .filter(s -> s.getCategory() == PaymentScheduleCategory.INSTALLMENT)
                        .toList();
        if (contract != null && !installments.isEmpty()
                && installments.stream().allMatch(s -> s.getStatus() == PaymentScheduleStatus.PAID)
                && contract.getStatus() == ContractStatus.ACTIVE) {
            contract.setStatus(ContractStatus.COMPLETED);
            contractRepository.save(contract);
        }

        return PaymentScheduleResponse.from(saved);
    }

    @Transactional
    public void handle(DeletePaymentScheduleCommand cmd) {
        if (!paymentScheduleRepository.existsById(cmd.scheduleId())) {
            throw new PaymentScheduleNotFoundException(cmd.scheduleId());
        }
        paymentScheduleRepository.deleteById(cmd.scheduleId());
    }

    /** Mỗi giờ refresh trạng thái OVERDUE cho các kỳ thanh toán quá hạn. */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void refreshOverduePayments() {
        int updated = paymentScheduleRepository.markOverdueBefore(LocalDate.now());
        if (updated > 0) {
            log.info("Marked {} payment schedules as OVERDUE", updated);
        }
    }
}
