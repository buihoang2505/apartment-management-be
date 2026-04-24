package com.apartment.interfaces.user.request;

public record UpdateProfileRequest(
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
