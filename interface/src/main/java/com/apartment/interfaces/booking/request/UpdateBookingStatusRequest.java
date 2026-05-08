package com.apartment.interfaces.booking.request;

import com.apartment.domain.booking.BookingStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateBookingStatusRequest(@NotNull BookingStatus status) {}
