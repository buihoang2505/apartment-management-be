package com.apartment.domain.zone;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BuildingRepository extends JpaRepository<Building, UUID> {
    List<Building> findByZoneId(UUID zoneId);
    boolean existsByZoneIdAndCode(UUID zoneId, String code);
}