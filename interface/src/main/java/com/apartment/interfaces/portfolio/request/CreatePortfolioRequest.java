package com.apartment.interfaces.portfolio.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record CreatePortfolioRequest(
        @NotBlank(message = "Tên danh mục không được để trống")
        @Size(max = 255)
        String name,

        String description,
        List<UUID> zoneIds
) {}