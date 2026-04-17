package com.apartment.interfaces.zone;

import com.apartment.app.zone.command.*;
import com.apartment.app.zone.dto.BuildingResponse;
import com.apartment.app.zone.dto.ZoneResponse;
import com.apartment.app.zone.handler.ZoneCommandHandler;
import com.apartment.app.zone.handler.ZoneQueryHandler;
import com.apartment.interfaces.shared.response.CommonResponse;
import com.apartment.interfaces.zone.request.CreateBuildingRequest;
import com.apartment.interfaces.zone.request.CreateZoneRequest;
import com.apartment.interfaces.zone.request.UpdateBuildingRequest;
import com.apartment.interfaces.zone.request.UpdateZoneRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Zones & Buildings", description = "Quản lý phân khu và tòa nhà")
@RestController
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneCommandHandler zoneCommandHandler;
    private final ZoneQueryHandler zoneQueryHandler;

    @GetMapping("/zones")
    public ResponseEntity<CommonResponse<List<ZoneResponse>>> getAll() {
        return ResponseEntity.ok(CommonResponse.ok(zoneQueryHandler.findAll()));
    }

    @GetMapping("/zones/{id}")
    public ResponseEntity<CommonResponse<ZoneResponse>> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(CommonResponse.ok(zoneQueryHandler.findById(id)));
    }

    @PostMapping("/zones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<ZoneResponse>> create(@Valid @RequestBody CreateZoneRequest request) {
        CreateZoneCommand cmd = new CreateZoneCommand(request.name(), request.code(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.ok("Tạo phân khu thành công", zoneCommandHandler.handle(cmd)));
    }

    @PutMapping("/zones/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<ZoneResponse>> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateZoneRequest request) {
        UpdateZoneCommand cmd = new UpdateZoneCommand(id, request.name(), request.code(), request.description());
        return ResponseEntity.ok(CommonResponse.ok("Cập nhật phân khu thành công", zoneCommandHandler.handle(cmd)));
    }

    @DeleteMapping("/zones/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable("id") UUID id) {
        zoneCommandHandler.handle(new DeleteZoneCommand(id));
        return ResponseEntity.ok(CommonResponse.ok("Xóa phân khu thành công", null));
    }

    @GetMapping("/zones/{id}/buildings")
    public ResponseEntity<CommonResponse<List<BuildingResponse>>> getBuildings(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(CommonResponse.ok(zoneQueryHandler.findBuildingsByZone(id)));
    }

    @PostMapping("/zones/{id}/buildings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<BuildingResponse>> createBuilding(
            @PathVariable("id") UUID id,
            @Valid @RequestBody CreateBuildingRequest request) {
        CreateBuildingCommand cmd = new CreateBuildingCommand(
                id, request.name(), request.code(), request.type(), request.totalFloors(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.ok("Tạo tòa nhà thành công", zoneCommandHandler.handle(cmd)));
    }

    @PutMapping("/zones/buildings/{buildingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<BuildingResponse>> updateBuilding(
            @PathVariable("buildingId") UUID buildingId,
            @Valid @RequestBody UpdateBuildingRequest request) {
        UpdateBuildingCommand cmd = new UpdateBuildingCommand(
                buildingId, request.name(), request.code(), request.type(), request.totalFloors(), request.description());
        return ResponseEntity.ok(CommonResponse.ok("Cập nhật tòa nhà thành công", zoneCommandHandler.handle(cmd)));
    }

    @DeleteMapping("/zones/buildings/{buildingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<Void>> deleteBuilding(@PathVariable("buildingId") UUID buildingId) {
        zoneCommandHandler.handle(new DeleteBuildingCommand(buildingId));
        return ResponseEntity.ok(CommonResponse.ok("Xóa tòa nhà thành công", null));
    }
}