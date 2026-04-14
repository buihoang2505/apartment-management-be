package com.apartment.interfaces.zone.request;

import com.apartment.domain.zone.BuildingType;
import jakarta.validation.constraints.*;

public record CreateBuildingRequest(
        @NotBlank(message = "Tên tòa nhà không được để trống")
        @Size(max = 255)
        String name,

        @NotBlank(message = "Mã tòa nhà không được để trống")
        @Size(max = 100)
        String code,

        BuildingType type,

        @Min(value = 1, message = "Số tầng tối thiểu là 1")
        Integer totalFloors,

        String description
) {}