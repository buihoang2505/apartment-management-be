package com.apartment.app.booking.handler;

import com.apartment.app.booking.dto.BookingResponse;
import com.apartment.app.booking.exception.BookingNotFoundException;
import com.apartment.domain.booking.BookingRepository;
import com.apartment.domain.booking.BookingStatus;
import com.apartment.domain.booking.BookingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingQueryHandler {

    private final BookingRepository bookingRepository;

    public List<BookingResponse> findInRange(
            LocalDateTime from, LocalDateTime to,
            BookingType type, BookingStatus status,
            UUID assignedToId, UUID customerId) {
        return bookingRepository.findInRange(from, to, type, status, assignedToId, customerId)
                .stream().map(BookingResponse::from).toList();
    }

    public BookingResponse findById(UUID id) {
        return bookingRepository.findById(id)
                .map(BookingResponse::from)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }
}
