package com.apartment.interfaces.apartment;

import com.apartment.app.apartment.command.*;
import com.apartment.app.apartment.dto.ApartmentResponse;
import com.apartment.app.apartment.dto.ApartmentStatusHistoryResponse;
import com.apartment.app.apartment.handler.ApartmentCommandHandler;
import com.apartment.app.apartment.handler.ApartmentQueryHandler;
import com.apartment.domain.apartment.ApartmentStatus;
import com.apartment.domain.apartment.ApartmentType;
import com.apartment.interfaces.apartment.request.CreateApartmentRequest;
import com.apartment.interfaces.apartment.request.MoveApartmentRequest;
import com.apartment.interfaces.apartment.request.UpdateApartmentRequest;
import com.apartment.interfaces.shared.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentCommandHandler apartmentCommandHandler;
    private final ApartmentQueryHandler apartmentQueryHandler;

    @GetMapping
    public ResponseEntity<CommonResponse<Page<ApartmentResponse>>> getAll(
            @RequestParam(required = false) UUID zoneId,
            @RequestParam(required = false) UUID buildingId,
            @RequestParam(required = false) ApartmentStatus status,
            @RequestParam(required = false) ApartmentType type,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(CommonResponse.ok(
                apartmentQueryHandler.findAll(zoneId, buildingId, status, type, search, page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ApartmentResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(CommonResponse.ok(apartmentQueryHandler.findById(id)));
    }

    @PostMapping
    public ResponseEntity<CommonResponse<ApartmentResponse>> create(
            @Valid @RequestBody CreateApartmentRequest request) {
        CreateApartmentCommand cmd = toCreateCommand(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.ok("Tạo căn hộ thành công", apartmentCommandHandler.handle(cmd)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<ApartmentResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateApartmentRequest request) {
        UpdateApartmentCommand cmd = toUpdateCommand(id, request);
        return ResponseEntity.ok(CommonResponse.ok("Cập nhật căn hộ thành công", apartmentCommandHandler.handle(cmd)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable UUID id) {
        apartmentCommandHandler.handle(new DeleteApartmentCommand(id));
        return ResponseEntity.ok(CommonResponse.ok("Xóa căn hộ thành công", null));
    }

    @GetMapping("/{id}/status-history")
    public ResponseEntity<CommonResponse<List<ApartmentStatusHistoryResponse>>> getStatusHistory(
            @PathVariable UUID id) {
        return ResponseEntity.ok(CommonResponse.ok(apartmentQueryHandler.findStatusHistory(id)));
    }

    @PatchMapping("/{id}/move")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<ApartmentResponse>> moveBuilding(
            @PathVariable UUID id,
            @Valid @RequestBody MoveApartmentRequest request) {
        MoveApartmentCommand cmd = new MoveApartmentCommand(id, request.newBuildingId(), request.note());
        return ResponseEntity.ok(CommonResponse.ok("Chuyển tòa nhà thành công", apartmentCommandHandler.handle(cmd)));
    }

    // ── mappers ──────────────────────────────────────────────────────────────

    private CreateApartmentCommand toCreateCommand(CreateApartmentRequest r) {
        List<CreateApartmentCommand.ImageCommand> images = r.images() == null ? null :
                r.images().stream()
                        .map(i -> new CreateApartmentCommand.ImageCommand(i.url(), i.label(), i.sortOrder()))
                        .toList();
        return new CreateApartmentCommand(r.unitCode(), r.displayCode(), r.area(), r.sellingPrice(),
                r.tax(), r.status(), r.furnitureDescription(), r.apartmentType(), r.floorNumber(),
                r.direction(), r.bedroomCount(), r.statusNote(), r.buildingId(), images);
    }

    private UpdateApartmentCommand toUpdateCommand(UUID id, UpdateApartmentRequest r) {
        List<CreateApartmentCommand.ImageCommand> images = r.images() == null ? null :
                r.images().stream()
                        .map(i -> new CreateApartmentCommand.ImageCommand(i.url(), i.label(), i.sortOrder()))
                        .toList();
        return new UpdateApartmentCommand(id, r.unitCode(), r.displayCode(), r.area(), r.sellingPrice(),
                r.tax(), r.status(), r.furnitureDescription(), r.apartmentType(), r.floorNumber(),
                r.direction(), r.bedroomCount(), r.statusNote(), r.buildingId(), images);
    }
}