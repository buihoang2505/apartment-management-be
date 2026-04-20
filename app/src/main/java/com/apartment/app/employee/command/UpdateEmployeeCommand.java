package com.apartment.app.employee.command;

import com.apartment.domain.employee.EmployeeStatus;

import java.util.UUID;

public record UpdateEmployeeCommand(
        UUID id,
        String fullName,
        String email,
        String phone,
        String position,
        UUID departmentId,
        EmployeeStatus status
) {}
