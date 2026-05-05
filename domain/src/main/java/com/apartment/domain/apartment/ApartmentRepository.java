package com.apartment.domain.apartment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApartmentRepository extends JpaRepository<Apartment, UUID>,
        JpaSpecificationExecutor<Apartment> {

    boolean existsByUnitCode(String unitCode);

    @Query("SELECT DISTINCT a FROM Apartment a LEFT JOIN FETCH a.images LEFT JOIN FETCH a.building b LEFT JOIN FETCH b.zone WHERE a.id = :id")
    Optional<Apartment> findByIdFetched(@Param("id") UUID id);

    @Query("SELECT a FROM Apartment a WHERE LOWER(a.unitCode) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(a.displayCode) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Apartment> searchByKeyword(@Param("q") String q, Pageable pageable);
}