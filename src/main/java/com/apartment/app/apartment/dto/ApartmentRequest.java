package com.apartment.app.apartment.dto;

import com.apartment.domain.apartment.ApartmentStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ApartmentRequest(
        @NotBlank(message = "Mã căn hộ không được để trống")
        @Size(max = 100)
        String unitCode,

        @Size(max = 100)
        String displayCode,

        @DecimalMin(value = "0.0", inclusive = false, message = "Diện tích phải lớn hơn 0")
        BigDecimal area,

        @DecimalMin(value = "0.0", inclusive = false, message = "Giá bán phải lớn hơn 0")
        BigDecimal sellingPrice,

        @DecimalMin(value = "0.0")
        @DecimalMax(value = "100.0")
        BigDecimal tax,

        ApartmentStatus status,

        String furnitureDescription,

        @NotNull(message = "ID tòa nhà không được để trống")
        UUID buildingId,

        List<ImageRequest> images
) {
    public record ImageRequest(
            @NotBlank String url,
            String label,
            int sortOrder
    ) {}
}