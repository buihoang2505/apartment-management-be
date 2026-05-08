package com.apartment.domain.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query("""
            SELECT c FROM Customer c
            LEFT JOIN FETCH c.assignedTo
            LEFT JOIN FETCH c.interestedApartment
            WHERE (:keyword IS NULL OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
                                     OR LOWER(COALESCE(c.email, '')) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
                                     OR c.phone LIKE CONCAT('%', CAST(:keyword AS string), '%'))
              AND (:status IS NULL OR c.status = :status)
              AND (:source IS NULL OR c.source = :source)
              AND (:assignedToId IS NULL OR c.assignedTo.id = :assignedToId)
            """)
    Page<Customer> search(
            @Param("keyword") String keyword,
            @Param("status") CustomerStatus status,
            @Param("source") LeadSource source,
            @Param("assignedToId") UUID assignedToId,
            Pageable pageable);

    @Query("SELECT c.status, COUNT(c) FROM Customer c GROUP BY c.status")
    List<Object[]> countGroupByStatus();
}
