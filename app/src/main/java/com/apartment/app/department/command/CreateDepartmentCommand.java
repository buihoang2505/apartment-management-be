package com.apartment.app.department.command;

public record CreateDepartmentCommand(
        String name,
        String code,
        String description
) {}
