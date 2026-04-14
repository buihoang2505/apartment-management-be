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
@Transactional
public class ZoneCommandHandler {

    private final ZoneRepository zoneRepository;
    private final BuildingRepository buildingRepository;

    public ZoneResponse handle(CreateZoneCommand cmd) {
        if (zoneRepository.existsByCode(cmd.code())) {
            throw new IllegalArgumentException("Mã phân khu '" + cmd.code() + "' đã tồn tại");
        }
        Zone zone = Zone.builder()
                .name(cmd.name())
                .code(cmd.code())
                .description(cmd.description())
                .build();
        return ZoneResponse.from(zoneRepository.save(zone));
    }

    public BuildingResponse handle(CreateBuildingCommand cmd) {
        Zone zone = zoneRepository.findById(cmd.zoneId())
                .orElseThrow(() -> new ZoneNotFoundException(cmd.zoneId()));

        if (buildingRepository.existsByZoneIdAndCode(cmd.zoneId(), cmd.code())) {
            throw new IllegalArgumentException("Mã tòa nhà '" + cmd.code() + "' đã tồn tại trong phân khu này");
        }

        Building building = Building.builder()
                .zone(zone)
                .name(cmd.name())
                .code(cmd.code())
                .type(cmd.type())
                .totalFloors(cmd.totalFloors())
                .description(cmd.description())
                .build();
        return BuildingResponse.from(buildingRepository.save(building));
    }

    public BuildingResponse handle(UpdateBuildingCommand cmd) {
        Building building = buildingRepository.findById(cmd.buildingId())
                .orElseThrow(() -> new BuildingNotFoundException(cmd.buildingId()));

        if (!building.getCode().equals(cmd.code()) &&
                buildingRepository.existsByZoneIdAndCode(building.getZone().getId(), cmd.code())) {
            throw new IllegalArgumentException("Mã tòa nhà '" + cmd.code() + "' đã tồn tại trong phân khu này");
        }

        building.setName(cmd.name());
        building.setCode(cmd.code());
        building.setType(cmd.type());
        building.setTotalFloors(cmd.totalFloors());
        building.setDescription(cmd.description());

        return BuildingResponse.from(buildingRepository.save(building));
    }

    public void handle(DeleteBuildingCommand cmd) {
        if (!buildingRepository.existsById(cmd.buildingId())) {
            throw new BuildingNotFoundException(cmd.buildingId());
        }
        buildingRepository.deleteById(cmd.buildingId());
    }
}