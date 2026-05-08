package com.apartment.app.customer.exception;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(UUID id) {
        super("Không tìm thấy khách hàng: " + id);
    }
}
