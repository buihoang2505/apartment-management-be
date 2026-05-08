package com.apartment.interfaces.customer.request;

import com.apartment.domain.customer.CustomerStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateCustomerStatusRequest(
        @NotNull(message = "Trạng thái không được để trống")
        CustomerStatus status
) {}
