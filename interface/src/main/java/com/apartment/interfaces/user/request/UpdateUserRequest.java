package com.apartment.interfaces.user.request;

import jakarta.validation.constraints.Pattern;

public record UpdateUserRequest(
        String fullName,
        String email,
        String phone,

        @Pattern(regexp = "^(ADMIN|MANAGER)$", message = "Role phải là ADMIN hoặc MANAGER")
        String role,

        boolean active
) {}