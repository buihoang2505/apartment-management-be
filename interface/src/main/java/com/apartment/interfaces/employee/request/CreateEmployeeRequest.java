package com.apartment.interfaces.employee.request;

import com.apartment.domain.employee.EmployeeStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateEmployeeRequest(
        @NotBlank(message = "Họ tên không được để trống")
        @Size(max = 255)
        String fullName,

        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không hợp lệ")
        String email,

        @Size(max = 20)
        String phone,

        @Size(max = 255)
        String position,

        UUID departmentId,
        EmployeeStatus status
) {}
