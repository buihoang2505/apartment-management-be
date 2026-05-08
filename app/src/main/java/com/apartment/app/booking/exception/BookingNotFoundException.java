package com.apartment.app.booking.exception;

import java.util.UUID;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(UUID id) {
        super("Không tìm thấy lịch hẹn: " + id);
    }
}
