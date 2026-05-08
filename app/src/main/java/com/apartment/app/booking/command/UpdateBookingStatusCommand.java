package com.apartment.app.booking.command;

import com.apartment.domain.booking.BookingStatus;

import java.util.UUID;

public record UpdateBookingStatusCommand(UUID id, BookingStatus status) {}
