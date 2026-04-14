package com.apartment.interfaces.apartment.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MoveApartmentRequest(
        @NotNull(message = "ID tòa nhà mới không được để trống")
        UUID newBuildingId,

        String note
) {}