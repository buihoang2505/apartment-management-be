package com.apartment.app.employee.exception;

import java.util.UUID;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(UUID id) {
        super("Không tìm thấy nhân viên: " + id);
    }
}
