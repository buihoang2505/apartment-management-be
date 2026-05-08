package com.apartment.app.booking.command;

import com.apartment.domain.booking.BookingType;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateBookingCommand(
        UUID id,
        String title,
        BookingType type,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String location,
        UUID customerId,
        UUID apartmentId,
        UUID assignedToId,
        String note
) {}
