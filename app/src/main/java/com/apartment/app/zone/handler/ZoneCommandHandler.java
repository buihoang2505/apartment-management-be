package com.apartment.app.zone.handler;

import com.apartment.app.zone.command.*;
import com.apartment.app.zone.dto.BuildingResponse;
import com.apartment.app.zone.dto.ZoneResponse;
import com.apartment.app.zone.exception.BuildingNotFoundException;
import com.apartment.app.zone.exception.ZoneNotFoundException;
import com.apartment.domain.audit.AuditLog;
import com.apartment.domain.audit.AuditLogRepository;
import com.apartment.domain.user.User;
import com.apartment.domain.user.UserRepository;
import com.apartment.domain.zone.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ZoneCommandHandler {

    private final ZoneRepository zoneRepository;
    private final BuildingRepository buildingRepository;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

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
        Zone saved = zoneRepository.save(zone);
        writeAudit("CREATE_ZONE", "Zone", saved.getId().toString(), null,
                zoneSnapshot(saved));
        return ZoneResponse.from(saved);
    }

    @Transactional
    public ZoneResponse handle(UpdateZoneCommand cmd) {
        Zone zone = zoneRepository.findById(cmd.id())
                .orElseThrow(() -> new ZoneNotFoundException(cmd.id()));

        var code = cmd.code().toUpperCase();
        if (!zone.getCode().equalsIgnoreCase(code) && zoneRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Mã phân khu '" + code + "' đã tồn tại");
        }

        String oldValue = zoneSnapshot(zone);
        zone.setName(cmd.name());
        zone.setCode(code);
        zone.setDescription(cmd.description());
        Zone saved = zoneRepository.save(zone);
        writeAudit("UPDATE_ZONE", "Zone", saved.getId().toString(), oldValue,
                zoneSnapshot(saved));
        return ZoneResponse.from(saved);
    }

    @Transactional
    public void handle(DeleteZoneCommand cmd) {
        Zone zone = zoneRepository.findById(cmd.id())
                .orElseThrow(() -> new ZoneNotFoundException(cmd.id()));
        String oldValue = zoneSnapshot(zone);
        zoneRepository.deleteById(cmd.id());
        writeAudit("DELETE_ZONE", "Zone", cmd.id().toString(), oldValue, null);
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
        Building saved = buildingRepository.save(building);
        writeAudit("CREATE_BUILDING", "Building", saved.getId().toString(), null,
                buildingSnapshot(saved));
        return BuildingResponse.from(saved);
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

        String oldValue = buildingSnapshot(building);
        building.setName(cmd.name());
        building.setCode(code);
        building.setType(cmd.type());
        building.setTotalFloors(cmd.totalFloors());
        building.setDescription(cmd.description());
        Building saved = buildingRepository.save(building);
        writeAudit("UPDATE_BUILDING", "Building", saved.getId().toString(), oldValue,
                buildingSnapshot(saved));
        return BuildingResponse.from(saved);
    }

    @Transactional
    public void handle(DeleteBuildingCommand cmd) {
        Building building = buildingRepository.findById(cmd.buildingId())
                .orElseThrow(() -> new BuildingNotFoundException(cmd.buildingId()));
        String oldValue = buildingSnapshot(building);
        buildingRepository.deleteById(cmd.buildingId());
        writeAudit("DELETE_BUILDING", "Building", cmd.buildingId().toString(), oldValue, null);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private void writeAudit(String action, String entityType, String entityId,
                            String oldValue, String newValue) {
        try {
            AuditLog log = AuditLog.builder()
                    .user(getCurrentUser())
                    .action(action)
                    .entityType(entityType)
                    .entityId(entityId)
                    .oldValue(oldValue)
                    .newValue(newValue)
                    .build();
            auditLogRepository.save(log);
        } catch (Exception ex) {
            // audit failure must not affect the main operation
        }
    }

    private static String zoneSnapshot(Zone z) {
        return "name=" + z.getName() + ", code=" + z.getCode() + ", description=" + z.getDescription();
    }

    private static String buildingSnapshot(Building b) {
        return "name=" + b.getName() + ", code=" + b.getCode()
                + ", type=" + b.getType() + ", totalFloors=" + b.getTotalFloors()
                + ", description=" + b.getDescription();
    }

    private User getCurrentUser() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return userRepository.findByUsername(username).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}
