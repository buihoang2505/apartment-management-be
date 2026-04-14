package com.apartment.app.user.command;

public record CreateUserCommand(
        String username,
        String password,
        String fullName,
        String email,
        String phone,
        String role
) {}