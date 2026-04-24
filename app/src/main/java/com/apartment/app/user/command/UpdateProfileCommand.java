package com.apartment.app.user.command;

import java.util.UUID;

public record UpdateProfileCommand(
        UUID userId,
        String fullName,
        String headline,
        String biography,
        String language,
        String website,
        String facebook,
        String instagram,
        String linkedin,
        String tiktok,
        String github
) {}
