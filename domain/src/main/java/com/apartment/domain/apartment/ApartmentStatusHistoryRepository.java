package com.apartment.domain.apartment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApartmentStatusHistoryRepository extends JpaRepository<ApartmentStatusHistory, UUID> {
    List<ApartmentStatusHistory> findByApartmentIdOrderByCreatedAtDesc(UUID apartmentId);
}