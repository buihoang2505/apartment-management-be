package com.apartment.app.zone.handler;

import com.apartment.app.zone.command.*;
import com.apartment.app.zone.dto.BuildingResponse;
import com.apartment.app.zone.dto.ZoneResponse;
import com.apartment.app.zone.exception.BuildingNotFoundException;
import com.apartment.app.zone.exception.ZoneNotFoundException;
import com.apartment.domain.zone.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ZoneCommandHandler {

    private final ZoneRepository zoneRepository;
    private final BuildingRepository buildingRepository;

    @Transactional
    public ZoneResponse handle(CreateZoneCommand cmd) {
        var code = cmd.code().toUpperCase();
        if (zoneRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Mã phân khu '" + code + "' đã tồn tại");
        }
        Zone zone = Zone.builder()
                .name(cmd.name())
                .code(code)
                .description(cmd.description())
                .build();
        return ZoneResponse.from(zoneRepository.save(zone));
    }

    @Transactional
    public ZoneResponse handle(UpdateZoneCommand cmd) {
        Zone zone = zoneRepository.findById(cmd.id())
                .orElseThrow(() -> new ZoneNotFoundException(cmd.id()));

        var code = cmd.code().toUpperCase();
        if (!zone.getCode().equalsIgnoreCase(code) && zoneRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Mã phân khu '" + code + "' đã tồn tại");
        }

        zone.setName(cmd.name());
        zone.setCode(code);
        zone.setDescription(cmd.description());
        return ZoneResponse.from(zoneRepository.save(zone));
    }

    @Transactional
    public void handle(DeleteZoneCommand cmd) {
        if (!zoneRepository.existsById(cmd.id())) {
            throw new ZoneNotFoundException(cmd.id());
        }
        zoneRepository.deleteById(cmd.id());
    }

    @Transactional
    public BuildingResponse handle(CreateBuildingCommand cmd) {
        Zone zone = zoneRepository.findById(cmd.zoneId())
                .orElseThrow(() -> new ZoneNotFoundException(cmd.zoneId()));

        var code = cmd.code().toUpperCase();
        if (buildingRepository.existsByZoneIdAndCode(cmd.zoneId(), code)) {
            throw new IllegalArgumentException("Mã tòa nhà '" + code + "' đã tồn tại trong phân khu này");
        }

        Building building = Building.builder()
                .zone(zone)
                .name(cmd.name())
                .code(code)
                .type(cmd.type())
                .totalFloors(cmd.totalFloors())
                .description(cmd.description())
                .build();
        return BuildingResponse.from(buildingRepository.save(building));
    }

    @Transactional
    public BuildingResponse handle(UpdateBuildingCommand cmd) {
        Building building = buildingRepository.findById(cmd.buildingId())
                .orElseThrow(() -> new BuildingNotFoundException(cmd.buildingId()));

        var code = cmd.code().toUpperCase();
        if (!building.getCode().equalsIgnoreCase(code) &&
                buildingRepository.existsByZoneIdAndCode(building.getZone().getId(), code)) {
            throw new IllegalArgumentException("Mã tòa nhà '" + code + "' đã tồn tại trong phân khu này");
        }

        building.setName(cmd.name());
        building.setCode(code);
        building.setType(cmd.type());
        building.setTotalFloors(cmd.totalFloors());
        building.setDescription(cmd.description());
        return BuildingResponse.from(buildingRepository.save(building));
    }

    @Transactional
    public void handle(DeleteBuildingCommand cmd) {
        if (!buildingRepository.existsById(cmd.buildingId())) {
            throw new BuildingNotFoundException(cmd.buildingId());
        }
        buildingRepository.deleteById(cmd.buildingId());
    }
}
