package com.apartment.interfaces.zone.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateZoneRequest(
        @NotBlank(message = "Tên phân khu không được để trống")
        @Size(max = 255)
        String name,

        @NotBlank(message = "Mã phân khu không được để trống")
        @Size(max = 100)
        @Pattern(regexp = "^[-A-Z0-9_]+$", message = "Mã chỉ gồm chữ hoa, số, gạch ngang, gạch dưới")
        String code,

        String description
) {}