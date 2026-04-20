package com.apartment.app.department.dto;

import com.apartment.domain.department.Department;

import java.time.LocalDateTime;
import java.util.UUID;

public record DepartmentResponse(
        UUID id,
        String name,
        String code,
        String description,
        long employeeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static DepartmentResponse from(Department department, long employeeCount) {
        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getCode(),
                department.getDescription(),
                employeeCount,
                department.getCreatedAt(),
                department.getUpdatedAt()
        );
    }
}
