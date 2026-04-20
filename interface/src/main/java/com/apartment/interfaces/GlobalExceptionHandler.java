package com.apartment.interfaces;

import com.apartment.app.apartment.exception.ApartmentNotFoundException;
import com.apartment.app.apartment.exception.DuplicateUnitCodeException;
import com.apartment.app.auth.exception.InvalidCredentialsException;
import com.apartment.app.department.exception.DepartmentNotFoundException;
import com.apartment.app.department.exception.DuplicateDepartmentCodeException;
import com.apartment.app.employee.exception.EmployeeNotFoundException;
import com.apartment.app.portfolio.exception.PortfolioNotFoundException;
import com.apartment.app.user.exception.UserNotFoundException;
import com.apartment.app.zone.exception.BuildingNotFoundException;
import com.apartment.app.zone.exception.ZoneNotFoundException;
import com.apartment.interfaces.shared.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── 404 Not Found ────────────────────────────────────────────────────────

    @ExceptionHandler({
            ApartmentNotFoundException.class,
            ZoneNotFoundException.class,
            BuildingNotFoundException.class,
            PortfolioNotFoundException.class,
            UserNotFoundException.class,
            DepartmentNotFoundException.class,
            EmployeeNotFoundException.class
    })
    public ResponseEntity<CommonResponse<Void>> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonResponse.error(ex.getMessage()));
    }

    // ── 400 / 409 Bad Request / Conflict ─────────────────────────────────────

    @ExceptionHandler({DuplicateUnitCodeException.class, DuplicateDepartmentCodeException.class})
    public ResponseEntity<CommonResponse<Void>> handleDuplicate(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(CommonResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(CommonResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid",
                        (a, b) -> a
                ));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse<>(false, "Dữ liệu đầu vào không hợp lệ", errors));
    }

    // ── 401 Unauthorized ─────────────────────────────────────────────────────

    @ExceptionHandler({BadCredentialsException.class, InvalidCredentialsException.class})
    public ResponseEntity<CommonResponse<Void>> handleUnauthorized(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponse.error("Tên đăng nhập hoặc mật khẩu không đúng"));
    }

    // ── 403 Forbidden ─────────────────────────────────────────────────────────

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(CommonResponse.error("Bạn không có quyền thực hiện thao tác này"));
    }

    // ── 500 Internal Server Error ─────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.error("Lỗi hệ thống"));
    }
}