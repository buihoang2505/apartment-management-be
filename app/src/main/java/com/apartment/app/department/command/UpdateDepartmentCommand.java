package com.apartment.app.department.command;

import java.util.UUID;

public record UpdateDepartmentCommand(
        UUID id,
        String name,
        String code,
        String description
) {}
