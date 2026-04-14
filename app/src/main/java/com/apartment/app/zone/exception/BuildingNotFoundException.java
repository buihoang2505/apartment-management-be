package com.apartment.app.zone.exception;

import java.util.UUID;

public class BuildingNotFoundException extends RuntimeException {
    public BuildingNotFoundException(UUID id) {
        super("Tòa nhà không tồn tại: " + id);
    }
}