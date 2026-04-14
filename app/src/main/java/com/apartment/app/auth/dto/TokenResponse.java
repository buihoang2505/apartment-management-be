package com.apartment.app.auth.dto;

public record TokenResponse(
        String token,
        String username,
        String role,
        long expiresIn
) {}