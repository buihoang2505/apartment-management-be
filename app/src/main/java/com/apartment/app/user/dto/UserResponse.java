package com.apartment.app.user.dto;

import com.apartment.domain.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String fullName,
        String email,
        String phone,
        String role,
        boolean active,
        String avatarUrl,
        LocalDateTime createdAt,
        String headline,
        String biography,
        String language,
        String website,
        String facebook,
        String instagram,
        String linkedin,
        String tiktok,
        String github
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.isActive(),
                user.getAvatarUrl(),
                user.getCreatedAt(),
                user.getHeadline(),
                user.getBiography(),
                user.getLanguage(),
                user.getWebsite(),
                user.getFacebook(),
                user.getInstagram(),
                user.getLinkedin(),
                user.getTiktok(),
                user.getGithub()
        );
    }
}
