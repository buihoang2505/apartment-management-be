package com.apartment.app.apartment.exception;

import java.util.UUID;

public class ApartmentImageNotFoundException extends RuntimeException {
    public ApartmentImageNotFoundException(UUID id) {
        super("Apartment image not found: " + id);
    }
}
