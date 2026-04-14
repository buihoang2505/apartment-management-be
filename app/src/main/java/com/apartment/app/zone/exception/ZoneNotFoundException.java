package com.apartment.app.zone.exception;

import java.util.UUID;

public class ZoneNotFoundException extends RuntimeException {
    public ZoneNotFoundException(UUID id) {
        super("Phân khu không tồn tại: " + id);
    }
}