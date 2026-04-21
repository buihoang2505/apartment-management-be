package com.apartment.domain.apartment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApartmentImageRepository extends JpaRepository<ApartmentImage, UUID> {
    Optional<ApartmentImage> findByIdAndApartment_Id(UUID id, UUID apartmentId);
}
