package com.apartment.app.auth.dto;

public record LoginResponse(
        String token,
        String username,
        String role,
        long expiresIn
) {}