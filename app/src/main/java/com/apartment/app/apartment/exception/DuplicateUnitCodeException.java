package com.apartment.app.apartment.exception;

public class DuplicateUnitCodeException extends RuntimeException {
    public DuplicateUnitCodeException(String unitCode) {
        super("Mã căn hộ '" + unitCode + "' đã tồn tại");
    }
}