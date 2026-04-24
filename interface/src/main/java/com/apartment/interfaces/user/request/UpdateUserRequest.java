package com.apartment.interfaces.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UpdateUserRequest(
        String fullName,

        @Email(message = "Email không hợp lệ")
        String email,

        @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại phải có 10-11 chữ số")
        String phone,

        @Pattern(regexp = "^(ADMIN|MANAGER)$", message = "Role phải là ADMIN hoặc MANAGER")
        String role,

        boolean active,
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
