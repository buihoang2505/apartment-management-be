package com.apartment.interfaces.employee;

import com.apartment.app.employee.command.CreateEmployeeCommand;
import com.apartment.app.employee.command.DeleteEmployeeCommand;
import com.apartment.app.employee.command.UpdateEmployeeCommand;
import com.apartment.app.employee.dto.DepartmentEmployeeCountResponse;
import com.apartment.app.employee.dto.EmployeeResponse;
import com.apartment.app.employee.handler.EmployeeCommandHandler;
import com.apartment.app.employee.handler.EmployeeQueryHandler;
import com.apartment.interfaces.employee.request.CreateEmployeeRequest;
import com.apartment.interfaces.employee.request.UpdateEmployeeRequest;
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

import java.util.List;
import java.util.UUID;

@Tag(name = "Employees", description = "Quản lý nhân viên")
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeCommandHandler employeeCommandHandler;
    private final EmployeeQueryHandler employeeQueryHandler;

    @Operation(summary = "Lấy danh sách nhân viên (có phân trang, filter theo phòng ban)")
    @GetMapping
    public ResponseEntity<CommonResponse<Page<EmployeeResponse>>> findAll(
            @RequestParam(value = "departmentId", required = false) UUID departmentId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(CommonResponse.ok(employeeQueryHandler.findAll(departmentId, pageable)));
    }

    @Operation(summary = "Thống kê nhân viên theo phòng ban")
    @GetMapping("/count-by-department")
    public ResponseEntity<CommonResponse<List<DepartmentEmployeeCountResponse>>> countByDepartment() {
        return ResponseEntity.ok(CommonResponse.ok(employeeQueryHandler.countByDepartment()));
    }

    @Operation(summary = "Lấy nhân viên theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<EmployeeResponse>> findById(
            @PathVariable("id") UUID id) {
        return ResponseEntity.ok(CommonResponse.ok(employeeQueryHandler.findById(id)));
    }

    @Operation(summary = "Tạo nhân viên mới")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<EmployeeResponse>> create(
            @Valid @RequestBody CreateEmployeeRequest request) {
        var cmd = new CreateEmployeeCommand(
                request.fullName(), request.email(), request.phone(),
                request.position(), request.departmentId(), request.status());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.ok("Tạo nhân viên thành công", employeeCommandHandler.handle(cmd)));
    }

    @Operation(summary = "Cập nhật nhân viên")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<EmployeeResponse>> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateEmployeeRequest request) {
        var cmd = new UpdateEmployeeCommand(
                id, request.fullName(), request.email(), request.phone(),
                request.position(), request.departmentId(), request.status());
        return ResponseEntity.ok(CommonResponse.ok("Cập nhật nhân viên thành công", employeeCommandHandler.handle(cmd)));
    }

    @Operation(summary = "Xóa nhân viên")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable("id") UUID id) {
        employeeCommandHandler.handle(new DeleteEmployeeCommand(id));
        return ResponseEntity.ok(CommonResponse.ok("Xóa nhân viên thành công", null));
    }
}
