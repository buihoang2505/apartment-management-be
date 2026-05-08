package com.apartment.interfaces.booking;

import com.apartment.app.booking.command.*;
import com.apartment.app.booking.dto.BookingResponse;
import com.apartment.app.booking.handler.BookingCommandHandler;
import com.apartment.app.booking.handler.BookingQueryHandler;
import com.apartment.domain.booking.BookingStatus;
import com.apartment.domain.booking.BookingType;
import com.apartment.interfaces.booking.request.CreateBookingRequest;
import com.apartment.interfaces.booking.request.UpdateBookingRequest;
import com.apartment.interfaces.booking.request.UpdateBookingStatusRequest;
import com.apartment.interfaces.shared.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingCommandHandler commandHandler;
    private final BookingQueryHandler queryHandler;

    @GetMapping
    public ResponseEntity<CommonResponse<List<BookingResponse>>> list(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(name = "type", required = false) BookingType type,
            @RequestParam(name = "status", required = false) BookingStatus status,
            @RequestParam(name = "assignedToId", required = false) UUID assignedToId,
            @RequestParam(name = "customerId", required = false) UUID customerId) {
        return ResponseEntity.ok(CommonResponse.ok("OK",
                queryHandler.findInRange(from, to, type, status, assignedToId, customerId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<BookingResponse>> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(CommonResponse.ok("OK", queryHandler.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CommonResponse<BookingResponse>> create(@Valid @RequestBody CreateBookingRequest req) {
        var created = commandHandler.handle(new CreateBookingCommand(
                req.title(), req.type(), req.startTime(), req.endTime(), req.location(),
                req.customerId(), req.apartmentId(), req.assignedToId(), req.note()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.ok("Đã tạo lịch hẹn", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CommonResponse<BookingResponse>> update(
            @PathVariable("id") UUID id, @Valid @RequestBody UpdateBookingRequest req) {
        var updated = commandHandler.handle(new UpdateBookingCommand(
                id, req.title(), req.type(), req.startTime(), req.endTime(), req.location(),
                req.customerId(), req.apartmentId(), req.assignedToId(), req.note()));
        return ResponseEntity.ok(CommonResponse.ok("Đã cập nhật", updated));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CommonResponse<BookingResponse>> updateStatus(
            @PathVariable("id") UUID id, @Valid @RequestBody UpdateBookingStatusRequest req) {
        var updated = commandHandler.handle(new UpdateBookingStatusCommand(id, req.status()));
        return ResponseEntity.ok(CommonResponse.ok("Đã cập nhật trạng thái", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable("id") UUID id) {
        commandHandler.handle(new DeleteBookingCommand(id));
        return ResponseEntity.ok(CommonResponse.ok("Đã xoá lịch hẹn", null));
    }
}
