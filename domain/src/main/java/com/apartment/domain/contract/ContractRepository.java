package com.apartment.domain.contract;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContractRepository extends JpaRepository<Contract, UUID> {

    @Query("""
            SELECT c FROM Contract c
            LEFT JOIN FETCH c.apartment
            LEFT JOIN FETCH c.customer
            WHERE (:keyword IS NULL OR LOWER(c.code) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')))
              AND (:type IS NULL OR c.type = :type)
              AND (:status IS NULL OR c.status = :status)
            """)
    Page<Contract> search(
            @Param("keyword") String keyword,
            @Param("type") ContractType type,
            @Param("status") ContractStatus status,
            Pageable pageable);

    @EntityGraph(attributePaths = {"apartment", "customer", "schedules"})
    Optional<Contract> findWithSchedulesById(UUID id);

    @Query("SELECT c.status, COUNT(c) FROM Contract c GROUP BY c.status")
    List<Object[]> countGroupByStatus();

    @Query("""
            SELECT COALESCE(SUM(s.amount - COALESCE(s.paidAmount, 0)), 0)
            FROM PaymentSchedule s
            WHERE s.status <> com.apartment.domain.contract.PaymentScheduleStatus.PAID
              AND s.category = com.apartment.domain.contract.PaymentScheduleCategory.INSTALLMENT
              AND s.contract.status = com.apartment.domain.contract.ContractStatus.ACTIVE
            """)
    BigDecimal sumOutstanding();

    @Query("""
            SELECT COALESCE(SUM(s.amount - COALESCE(s.paidAmount, 0)), 0)
            FROM PaymentSchedule s
            WHERE s.status = com.apartment.domain.contract.PaymentScheduleStatus.OVERDUE
              AND s.category = com.apartment.domain.contract.PaymentScheduleCategory.INSTALLMENT
              AND s.contract.status = com.apartment.domain.contract.ContractStatus.ACTIVE
            """)
    BigDecimal sumOverdue();

    boolean existsByCode(String code);
}
