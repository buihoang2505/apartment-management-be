package com.apartment.interfaces.customer.request;

import com.apartment.domain.customer.CustomerStatus;
import com.apartment.domain.customer.LeadSource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateCustomerRequest(
        @NotBlank(message = "Họ tên không được để trống")
        @Size(max = 255)
        String fullName,

        @Email(message = "Email không hợp lệ")
        @Size(max = 255)
        String email,

        @NotBlank(message = "Số điện thoại không được để trống")
        @Size(max = 20)
        String phone,

        LeadSource source,
        CustomerStatus status,
        UUID assignedToId,
        UUID interestedApartmentId,

        @Size(max = 2000)
        String note
) {}
