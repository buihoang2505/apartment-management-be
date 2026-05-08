package com.apartment.app.contract.exception;

import java.util.UUID;

public class PaymentScheduleNotFoundException extends RuntimeException {
    public PaymentScheduleNotFoundException(UUID id) {
        super("Không tìm thấy kỳ thanh toán: " + id);
    }
}
