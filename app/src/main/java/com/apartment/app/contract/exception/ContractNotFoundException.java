package com.apartment.app.contract.exception;

import java.util.UUID;

public class ContractNotFoundException extends RuntimeException {
    public ContractNotFoundException(UUID id) {
        super("Không tìm thấy hợp đồng: " + id);
    }
}
