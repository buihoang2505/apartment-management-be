package com.apartment.app.department.exception;

public class DuplicateDepartmentCodeException extends RuntimeException {
    public DuplicateDepartmentCodeException(String code) {
        super("Mã phòng ban đã tồn tại: " + code);
    }
}
