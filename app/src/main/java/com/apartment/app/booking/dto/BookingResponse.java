package com.apartment.app.booking.dto;

import com.apartment.domain.booking.Booking;
import com.apartment.domain.booking.BookingStatus;
import com.apartment.domain.booking.BookingType;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        String title,
        BookingType type,
        BookingStatus status,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String location,
        UUID customerId,
        String customerName,
        String customerPhone,
        UUID apartmentId,
        String apartmentCode,
        UUID assignedToId,
        String assignedToName,
        String note
) {
    public static BookingResponse from(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getTitle(),
                b.getType(),
                b.getStatus(),
                b.getStartTime(),
                b.getEndTime(),
                b.getLocation(),
                b.getCustomer() != null ? b.getCustomer().getId() : null,
                b.getCustomer() != null ? b.getCustomer().getFullName() : null,
                b.getCustomer() != null ? b.getCustomer().getPhone() : null,
                b.getApartment() != null ? b.getApartment().getId() : null,
                b.getApartment() != null ? b.getApartment().getUnitCode() : null,
                b.getAssignedTo() != null ? b.getAssignedTo().getId() : null,
                b.getAssignedTo() != null ? b.getAssignedTo().getFullName() : null,
                b.getNote()
        );
    }
}
