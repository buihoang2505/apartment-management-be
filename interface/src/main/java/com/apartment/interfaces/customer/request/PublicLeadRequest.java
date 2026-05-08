package com.apartment.interfaces.customer.request;

import com.apartment.domain.customer.LeadSource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record PublicLeadRequest(
        @NotBlank(message = "Họ tên không được để trống")
        @Size(max = 255)
        String fullName,

        @Email(message = "Email không hợp lệ")
        @Size(max = 255)
        String email,

        @NotBlank(message = "Số điện thoại không được để trống")
        @Pattern(regexp = "^[\\d\\s+()-]{8,20}$", message = "Số điện thoại không hợp lệ")
        String phone,

        LeadSource source,

        UUID interestedApartmentId,

        @Size(max = 1000)
        String note
) {}
