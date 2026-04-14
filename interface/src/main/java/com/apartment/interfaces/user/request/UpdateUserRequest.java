package com.apartment.interfaces.user.request;

public record UpdateUserRequest(
        String fullName,
        String email,
        String phone,
        String role,
        boolean active
) {}