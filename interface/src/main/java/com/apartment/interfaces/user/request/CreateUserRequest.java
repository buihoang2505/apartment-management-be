package com.apartment.interfaces.user.request;

import jakarta.validation.constraints.*;

public record CreateUserRequest(
        @NotBlank(message = "Username không được để trống")
        @Size(max = 100)
        String username,

        @NotBlank(message = "Password không được để trống")
        @Size(min = 6, message = "Password tối thiểu 6 ký tự")
        String password,

        String fullName,

        @Email(message = "Email không hợp lệ")
        String email,

        @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại phải có 10-11 chữ số")
        String phone,

        @Pattern(regexp = "^(ADMIN|MANAGER)$", message = "Role phải là ADMIN hoặc MANAGER")
        String role
) {}