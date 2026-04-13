package com.apartment.app.zone;

import com.apartment.app.zone.dto.*;
import com.apartment.domain.zone.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final BuildingRepository buildingRepository;

    public List<ZoneResponse> findAll() {
        return zoneRepository.findAll().stream()
                .map(ZoneResponse::from)
                .toList();
    }

    @Transactional
    public ZoneResponse create(ZoneRequest request) {
        if (zoneRepository.existsByCode(request.code())) {
            throw new IllegalArgumentException("Mã phân khu '" + request.code() + "' đã tồn tại");
        }
        Zone zone = Zone.builder()
                .name(request.name())
                .code(request.code())
                .description(request.description())
                .build();
        return ZoneResponse.from(zoneRepository.save(zone));
    }

    public List<BuildingResponse> findBuildingsByZone(UUID zoneId) {
        if (!zoneRepository.existsById(zoneId)) {
            throw new jakarta.persistence.EntityNotFoundException("Phân khu không tồn tại: " + zoneId);
        }
        return buildingRepository.findByZoneId(zoneId).stream()
                .map(BuildingResponse::from)
                .toList();
    }

    @Transactional
    public BuildingResponse createBuilding(UUID zoneId, BuildingRequest request) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Phân khu không tồn tại: " + zoneId));

        if (buildingRepository.existsByZoneIdAndCode(zoneId, request.code())) {
            throw new IllegalArgumentException("Mã tòa nhà '" + request.code() + "' đã tồn tại trong phân khu này");
        }

        Building building = Building.builder()
                .zone(zone)
                .name(request.name())
                .code(request.code())
                .build();
        return BuildingResponse.from(buildingRepository.save(building));
    }
}