package com.apartment.app.booking.handler;

import com.apartment.app.apartment.exception.ApartmentNotFoundException;
import com.apartment.app.booking.command.*;
import com.apartment.app.booking.dto.BookingResponse;
import com.apartment.app.booking.exception.BookingNotFoundException;
import com.apartment.app.customer.exception.CustomerNotFoundException;
import com.apartment.app.employee.exception.EmployeeNotFoundException;
import com.apartment.domain.apartment.ApartmentRepository;
import com.apartment.domain.booking.Booking;
import com.apartment.domain.booking.BookingRepository;
import com.apartment.domain.booking.BookingStatus;
import com.apartment.domain.customer.CustomerRepository;
import com.apartment.domain.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingCommandHandler {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final ApartmentRepository apartmentRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public BookingResponse handle(CreateBookingCommand cmd) {
        validateRange(cmd.startTime(), cmd.endTime());

        var builder = Booking.builder()
                .title(cmd.title())
                .type(cmd.type())
                .status(BookingStatus.SCHEDULED)
                .startTime(cmd.startTime())
                .endTime(cmd.endTime())
                .location(cmd.location())
                .note(cmd.note());

        if (cmd.customerId() != null) {
            builder.customer(customerRepository.findById(cmd.customerId())
                    .orElseThrow(() -> new CustomerNotFoundException(cmd.customerId())));
        }
        if (cmd.apartmentId() != null) {
            builder.apartment(apartmentRepository.findById(cmd.apartmentId())
                    .orElseThrow(() -> new ApartmentNotFoundException(cmd.apartmentId())));
        }
        if (cmd.assignedToId() != null) {
            builder.assignedTo(employeeRepository.findById(cmd.assignedToId())
                    .orElseThrow(() -> new EmployeeNotFoundException(cmd.assignedToId())));
        }

        return BookingResponse.from(bookingRepository.save(builder.build()));
    }

    @Transactional
    public BookingResponse handle(UpdateBookingCommand cmd) {
        validateRange(cmd.startTime(), cmd.endTime());

        var booking = bookingRepository.findById(cmd.id())
                .orElseThrow(() -> new BookingNotFoundException(cmd.id()));

        booking.setTitle(cmd.title());
        booking.setType(cmd.type());
        booking.setStartTime(cmd.startTime());
        booking.setEndTime(cmd.endTime());
        booking.setLocation(cmd.location());
        booking.setNote(cmd.note());

        booking.setCustomer(cmd.customerId() == null ? null
                : customerRepository.findById(cmd.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(cmd.customerId())));
        booking.setApartment(cmd.apartmentId() == null ? null
                : apartmentRepository.findById(cmd.apartmentId())
                .orElseThrow(() -> new ApartmentNotFoundException(cmd.apartmentId())));
        booking.setAssignedTo(cmd.assignedToId() == null ? null
                : employeeRepository.findById(cmd.assignedToId())
                .orElseThrow(() -> new EmployeeNotFoundException(cmd.assignedToId())));

        return BookingResponse.from(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse handle(UpdateBookingStatusCommand cmd) {
        var booking = bookingRepository.findById(cmd.id())
                .orElseThrow(() -> new BookingNotFoundException(cmd.id()));
        booking.setStatus(cmd.status());
        return BookingResponse.from(bookingRepository.save(booking));
    }

    @Transactional
    public void handle(DeleteBookingCommand cmd) {
        if (!bookingRepository.existsById(cmd.id())) {
            throw new BookingNotFoundException(cmd.id());
        }
        bookingRepository.deleteById(cmd.id());
    }

    private void validateRange(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Cần thời gian bắt đầu và kết thúc");
        }
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("Thời gian kết thúc phải sau thời gian bắt đầu");
        }
    }
}
