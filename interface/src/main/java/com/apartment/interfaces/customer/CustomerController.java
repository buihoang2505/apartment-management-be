package com.apartment.interfaces.customer;

import com.apartment.app.customer.command.CreateCustomerCommand;
import com.apartment.app.customer.command.DeleteCustomerCommand;
import com.apartment.app.customer.command.UpdateCustomerCommand;
import com.apartment.app.customer.command.UpdateCustomerStatusCommand;
import com.apartment.app.customer.dto.CustomerResponse;
import com.apartment.app.customer.dto.CustomerStatsResponse;
import com.apartment.app.customer.handler.CustomerCommandHandler;
import com.apartment.app.customer.handler.CustomerQueryHandler;
import com.apartment.domain.customer.CustomerStatus;
import com.apartment.domain.customer.LeadSource;
import com.apartment.interfaces.customer.request.CreateCustomerRequest;
import com.apartment.interfaces.customer.request.UpdateCustomerRequest;
import com.apartment.interfaces.customer.request.UpdateCustomerStatusRequest;
import com.apartment.interfaces.shared.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Customers", description = "Quản lý khách hàng / lead")
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerCommandHandler customerCommandHandler;
    private final CustomerQueryHandler customerQueryHandler;

    @Operation(summary = "Danh sách khách hàng (filter theo keyword/status/source/phụ trách)")
    @GetMapping
    public ResponseEntity<CommonResponse<Page<CustomerResponse>>> findAll(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) CustomerStatus status,
            @RequestParam(value = "source", required = false) LeadSource source,
            @RequestParam(value = "assignedToId", required = false) UUID assignedToId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(CommonResponse.ok(
                customerQueryHandler.findAll(keyword, status, source, assignedToId, pageable)));
    }

    @Operation(summary = "Thống kê khách hàng theo trạng thái (funnel)")
    @GetMapping("/stats")
    public ResponseEntity<CommonResponse<CustomerStatsResponse>> stats() {
        return ResponseEntity.ok(CommonResponse.ok(customerQueryHandler.getStats()));
    }

    @Operation(summary = "Lấy khách hàng theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<CustomerResponse>> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(CommonResponse.ok(customerQueryHandler.findById(id)));
    }

    @Operation(summary = "Tạo khách hàng mới")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<CommonResponse<CustomerResponse>> create(
            @Valid @RequestBody CreateCustomerRequest request) {
        var cmd = new CreateCustomerCommand(
                request.fullName(), request.email(), request.phone(),
                request.source(), request.status(),
                request.assignedToId(), request.interestedApartmentId(),
                request.note());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.ok("Tạo khách hàng thành công", customerCommandHandler.handle(cmd)));
    }

    @Operation(summary = "Cập nhật khách hàng")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<CommonResponse<CustomerResponse>> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateCustomerRequest request) {
        var cmd = new UpdateCustomerCommand(
                id, request.fullName(), request.email(), request.phone(),
                request.source(), request.status(),
                request.assignedToId(), request.interestedApartmentId(),
                request.note());
        return ResponseEntity.ok(CommonResponse.ok("Cập nhật khách hàng thành công", customerCommandHandler.handle(cmd)));
    }

    @Operation(summary = "Cập nhật trạng thái khách hàng")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<CommonResponse<CustomerResponse>> updateStatus(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateCustomerStatusRequest request) {
        var cmd = new UpdateCustomerStatusCommand(id, request.status());
        return ResponseEntity.ok(CommonResponse.ok("Cập nhật trạng thái thành công", customerCommandHandler.handle(cmd)));
    }

    @Operation(summary = "Xoá khách hàng")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable("id") UUID id) {
        customerCommandHandler.handle(new DeleteCustomerCommand(id));
        return ResponseEntity.ok(CommonResponse.ok("Xoá khách hàng thành công", null));
    }
}
