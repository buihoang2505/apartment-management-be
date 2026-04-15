package com.apartment.interfaces.zone.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateZoneRequest(
        @NotBlank(message = "Tên phân khu không được để trống")
        String name,

        @NotBlank(message = "Mã phân khu không được để trống")
        @Pattern(regexp = "^[-A-Z0-9_]+$", message = "Mã phân khu chỉ gồm chữ hoa, số, dấu gạch ngang hoặc gạch dưới")
        String code,

        String description
) {}