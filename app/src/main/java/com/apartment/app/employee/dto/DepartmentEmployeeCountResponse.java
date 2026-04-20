package com.apartment.app.employee.dto;

import java.util.UUID;

public record DepartmentEmployeeCountResponse(
        UUID departmentId,
        String departmentName,
        String departmentCode,
        long employeeCount
) {}
