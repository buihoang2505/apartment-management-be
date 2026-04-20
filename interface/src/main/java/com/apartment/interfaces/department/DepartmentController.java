package com.apartment.interfaces.department;

import com.apartment.app.department.command.CreateDepartmentCommand;
import com.apartment.app.department.command.DeleteDepartmentCommand;
import com.apartment.app.department.command.UpdateDepartmentCommand;
import com.apartment.app.department.dto.DepartmentResponse;
import com.apartment.app.department.handler.DepartmentCommandHandler;
import com.apartment.app.department.handler.DepartmentQueryHandler;
import com.apartment.interfaces.department.request.CreateDepartmentRequest;
import com.apartment.interfaces.department.request.UpdateDepartmentRequest;
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

@Tag(name = "Departments", description = "Quản lý phòng ban")
@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentCommandHandler departmentCommandHandler;
    private final DepartmentQueryHandler departmentQueryHandler;

    @Operation(summary = "Lấy danh sách phòng ban (có phân trang)")
    @GetMapping
    public ResponseEntity<CommonResponse<Page<DepartmentResponse>>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(CommonResponse.ok(departmentQueryHandler.findAll(pageable)));
    }

    @Operation(summary = "Lấy phòng ban theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<DepartmentResponse>> findById(
            @PathVariable("id") UUID id) {
        return ResponseEntity.ok(CommonResponse.ok(departmentQueryHandler.findById(id)));
    }

    @Operation(summary = "Tạo phòng ban mới")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<CommonResponse<DepartmentResponse>> create(
            @Valid @RequestBody CreateDepartmentRequest request) {
        var cmd = new CreateDepartmentCommand(request.name(), request.code(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.ok("Tạo phòng ban thành công", departmentCommandHandler.handle(cmd)));
    }

    @Operation(summary = "Cập nhật phòng ban")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<CommonResponse<DepartmentResponse>> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateDepartmentRequest request) {
        var cmd = new UpdateDepartmentCommand(id, request.name(), request.code(), request.description());
        return ResponseEntity.ok(CommonResponse.ok("Cập nhật phòng ban thành công", departmentCommandHandler.handle(cmd)));
    }

    @Operation(summary = "Xóa phòng ban")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable("id") UUID id) {
        departmentCommandHandler.handle(new DeleteDepartmentCommand(id));
        return ResponseEntity.ok(CommonResponse.ok("Xóa phòng ban thành công", null));
    }
}
