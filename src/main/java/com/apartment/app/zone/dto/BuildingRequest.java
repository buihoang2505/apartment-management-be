package com.apartment.app.zone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BuildingRequest(
        @NotBlank(message = "Tên tòa nhà không được để trống")
        @Size(max = 255)
        String name,

        @NotBlank(message = "Mã tòa nhà không được để trống")
        @Size(max = 100)
        String code
) {}