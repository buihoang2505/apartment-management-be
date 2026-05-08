package com.apartment.domain.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("""
            SELECT b FROM Booking b
            LEFT JOIN FETCH b.customer
            LEFT JOIN FETCH b.apartment
            LEFT JOIN FETCH b.assignedTo
            WHERE b.startTime < :to
              AND b.endTime > :from
              AND (:type IS NULL OR b.type = :type)
              AND (:status IS NULL OR b.status = :status)
              AND (:assignedToId IS NULL OR b.assignedTo.id = :assignedToId)
              AND (:customerId IS NULL OR b.customer.id = :customerId)
            ORDER BY b.startTime ASC
            """)
    List<Booking> findInRange(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("type") BookingType type,
            @Param("status") BookingStatus status,
            @Param("assignedToId") UUID assignedToId,
            @Param("customerId") UUID customerId);
}
