package com.apartment.domain.zone;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ZoneRepository extends JpaRepository<Zone, UUID> {
    boolean existsByCode(String code);
    Optional<Zone> findByCode(String code);
}