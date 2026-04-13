package com.apartment.web.apartment;

import com.apartment.app.apartment.ApartmentService;
import com.apartment.app.apartment.dto.ApartmentRequest;
import com.apartment.app.apartment.dto.ApartmentResponse;
import com.apartment.domain.apartment.ApartmentStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;

    @GetMapping
    public ResponseEntity<Page<ApartmentResponse>> getAll(
            @RequestParam(required = false) UUID zoneId,
            @RequestParam(required = false) UUID buildingId,
            @RequestParam(required = false) ApartmentStatus status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(apartmentService.findAll(zoneId, buildingId, status, search, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(apartmentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ApartmentResponse> create(@Valid @RequestBody ApartmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(apartmentService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApartmentResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ApartmentRequest request) {
        return ResponseEntity.ok(apartmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        apartmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}