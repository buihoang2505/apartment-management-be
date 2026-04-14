package com.apartment.app.apartment.exception;

import java.util.UUID;

public class ApartmentNotFoundException extends RuntimeException {
    public ApartmentNotFoundException(UUID id) {
        super("Căn hộ không tồn tại: " + id);
    }
}