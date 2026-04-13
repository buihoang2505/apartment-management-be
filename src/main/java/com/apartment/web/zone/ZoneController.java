package com.apartment.web.zone;

import com.apartment.app.zone.ZoneService;
import com.apartment.app.zone.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @GetMapping
    public ResponseEntity<List<ZoneResponse>> getAll() {
        return ResponseEntity.ok(zoneService.findAll());
    }

    @PostMapping
    public ResponseEntity<ZoneResponse> create(@Valid @RequestBody ZoneRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(zoneService.create(request));
    }

    @GetMapping("/{id}/buildings")
    public ResponseEntity<List<BuildingResponse>> getBuildings(@PathVariable UUID id) {
        return ResponseEntity.ok(zoneService.findBuildingsByZone(id));
    }

    @PostMapping("/{id}/buildings")
    public ResponseEntity<BuildingResponse> createBuilding(
            @PathVariable UUID id,
            @Valid @RequestBody BuildingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(zoneService.createBuilding(id, request));
    }
}