package com.apartment.domain.apartment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ApartmentRepository extends JpaRepository<Apartment, UUID>,
        JpaSpecificationExecutor<Apartment> {

    boolean existsByUnitCode(String unitCode);

    @Query("SELECT a.status, COUNT(a) FROM Apartment a GROUP BY a.status")
    List<Object[]> countByStatus();

    @Query("SELECT a.apartmentType, COUNT(a) FROM Apartment a WHERE a.apartmentType IS NOT NULL GROUP BY a.apartmentType")
    List<Object[]> countByType();

    @Query("SELECT COUNT(a) FROM Apartment a WHERE a.createdAt >= :from AND a.createdAt < :to")
    long countCreatedBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}