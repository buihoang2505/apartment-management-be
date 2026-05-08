package com.apartment.interfaces.booking.request;

import com.apartment.domain.booking.BookingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateBookingRequest(
        @NotBlank @Size(max = 255) String title,
        @NotNull BookingType type,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @Size(max = 500) String location,
        UUID customerId,
        UUID apartmentId,
        UUID assignedToId,
        @Size(max = 2000) String note
) {}
