package com.apartment.domain.contract;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, UUID> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE PaymentSchedule s
               SET s.status = com.apartment.domain.contract.PaymentScheduleStatus.OVERDUE
             WHERE s.status IN (com.apartment.domain.contract.PaymentScheduleStatus.PENDING,
                                com.apartment.domain.contract.PaymentScheduleStatus.PARTIAL)
               AND s.dueDate < :today
            """)
    int markOverdueBefore(@Param("today") LocalDate today);
}
