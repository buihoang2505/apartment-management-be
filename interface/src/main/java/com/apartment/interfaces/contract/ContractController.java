package com.apartment.interfaces.contract;

import com.apartment.app.contract.command.*;
import com.apartment.app.contract.dto.ContractResponse;
import com.apartment.app.contract.dto.ContractStatsResponse;
import com.apartment.app.contract.dto.PaymentScheduleResponse;
import com.apartment.app.contract.handler.ContractCommandHandler;
import com.apartment.app.contract.handler.ContractQueryHandler;
import com.apartment.domain.contract.ContractStatus;
import com.apartment.domain.contract.ContractType;
import com.apartment.interfaces.contract.request.AddPaymentScheduleRequest;
import com.apartment.interfaces.contract.request.CreateContractRequest;
import com.apartment.interfaces.contract.request.MarkPaymentPaidRequest;
import com.apartment.interfaces.contract.request.UpdateContractStatusRequest;
import com.apartment.interfaces.shared.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractCommandHandler commandHandler;
    private final ContractQueryHandler queryHandler;

    @GetMapping
    public ResponseEntity<CommonResponse<Page<ContractResponse>>> list(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "type", required = false) ContractType type,
            @RequestParam(name = "status", required = false) ContractStatus status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var result = queryHandler.findAll(keyword, type, status, pageable);
        return ResponseEntity.ok(CommonResponse.ok("OK", result));
    }

    @GetMapping("/stats")
    public ResponseEntity<CommonResponse<ContractStatsResponse>> stats() {
        return ResponseEntity.ok(CommonResponse.ok("OK", queryHandler.getStats()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ContractResponse>> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(CommonResponse.ok("OK", queryHandler.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CommonResponse<ContractResponse>> create(@Valid @RequestBody CreateContractRequest req) {
        var cmd = new CreateContractCommand(
                req.code(), req.type(), req.apartmentId(), req.customerId(),
                req.startDate(), req.endDate(), req.signedDate(),
                req.totalAmount(), req.monthlyAmount(), req.depositAmount(),
                req.numberOfInstallments(),
                req.note(),
                req.autoGenerateSchedule() != null && req.autoGenerateSchedule(),
                req.generateDepositRefund() != null && req.generateDepositRefund()
        );
        var created = commandHandler.handle(cmd);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.ok("Đã tạo hợp đồng", created));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CommonResponse<ContractResponse>> updateStatus(
            @PathVariable("id") UUID id, @Valid @RequestBody UpdateContractStatusRequest req) {
        var updated = commandHandler.handle(new UpdateContractStatusCommand(id, req.status()));
        return ResponseEntity.ok(CommonResponse.ok("Đã cập nhật trạng thái", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable("id") UUID id) {
        commandHandler.handle(new DeleteContractCommand(id));
        return ResponseEntity.ok(CommonResponse.ok("Đã xoá hợp đồng", null));
    }

    @PostMapping("/{id}/schedules")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CommonResponse<PaymentScheduleResponse>> addSchedule(
            @PathVariable("id") UUID id, @Valid @RequestBody AddPaymentScheduleRequest req) {
        var created = commandHandler.handle(new AddPaymentScheduleCommand(
                id, req.dueDate(), req.amount(), req.note()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.ok("Đã thêm kỳ thanh toán", created));
    }

    @PatchMapping("/schedules/{scheduleId}/pay")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CommonResponse<PaymentScheduleResponse>> markPaid(
            @PathVariable("scheduleId") UUID scheduleId, @Valid @RequestBody MarkPaymentPaidRequest req) {
        var updated = commandHandler.handle(new MarkPaymentPaidCommand(
                scheduleId, req.paidAmount(), req.paidDate(), req.note()));
        return ResponseEntity.ok(CommonResponse.ok("Đã ghi nhận thanh toán", updated));
    }

    @DeleteMapping("/schedules/{scheduleId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CommonResponse<Void>> deleteSchedule(@PathVariable("scheduleId") UUID scheduleId) {
        commandHandler.handle(new DeletePaymentScheduleCommand(scheduleId));
        return ResponseEntity.ok(CommonResponse.ok("Đã xoá kỳ thanh toán", null));
    }
}
