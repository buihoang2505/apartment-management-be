package com.apartment.app.user.command;

import java.util.UUID;

public record UpdateUserCommand(
        UUID id,
        String fullName,
        String email,
        String phone,
        String role,
        boolean active
) {}