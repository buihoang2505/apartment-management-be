package com.apartment.app.zone.handler;

import com.apartment.app.zone.dto.BuildingResponse;
import com.apartment.app.zone.dto.ZoneResponse;
import com.apartment.app.zone.exception.ZoneNotFoundException;
import com.apartment.domain.zone.BuildingRepository;
import com.apartment.domain.zone.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ZoneQueryHandler {

    private final ZoneRepository zoneRepository;
    private final BuildingRepository buildingRepository;

    public List<ZoneResponse> findAll() {
        return zoneRepository.findAll().stream()
                .map(ZoneResponse::from)
                .toList();
    }

    public List<BuildingResponse> findBuildingsByZone(UUID zoneId) {
        if (!zoneRepository.existsById(zoneId)) {
            throw new ZoneNotFoundException(zoneId);
        }
        return buildingRepository.findByZoneId(zoneId).stream()
                .map(BuildingResponse::from)
                .toList();
    }
}