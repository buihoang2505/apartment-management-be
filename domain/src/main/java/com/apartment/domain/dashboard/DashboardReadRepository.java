package com.apartment.domain.dashboard;

import com.apartment.domain.apartment.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DashboardReadRepository extends JpaRepository<Apartment, UUID> {

    @Query("""
            SELECT
                COUNT(a)                                                                              AS total,
                SUM(CASE WHEN a.createdAt >= :thisFrom AND a.createdAt < :thisTo THEN 1L ELSE 0L END) AS thisMonth,
                SUM(CASE WHEN a.createdAt >= :prevFrom AND a.createdAt < :prevTo THEN 1L ELSE 0L END) AS lastMonth,
                SUM(CASE WHEN a.status = com.apartment.domain.apartment.ApartmentStatus.DANG_BAN THEN 1L ELSE 0L END) AS dangBan,
                SUM(CASE WHEN a.status = com.apartment.domain.apartment.ApartmentStatus.DA_COC   THEN 1L ELSE 0L END) AS daCoc,
                SUM(CASE WHEN a.status = com.apartment.domain.apartment.ApartmentStatus.DA_BAN   THEN 1L ELSE 0L END) AS daBan,
                SUM(CASE WHEN a.status = com.apartment.domain.apartment.ApartmentStatus.GIU_CHO  THEN 1L ELSE 0L END) AS giuCho,
                SUM(CASE WHEN a.status = com.apartment.domain.apartment.ApartmentStatus.KHOA     THEN 1L ELSE 0L END) AS khoa,
                SUM(CASE WHEN a.apartmentType = com.apartment.domain.apartment.ApartmentType.STUDIO             THEN 1L ELSE 0L END) AS studio,
                SUM(CASE WHEN a.apartmentType = com.apartment.domain.apartment.ApartmentType.ONE_BEDROOM        THEN 1L ELSE 0L END) AS oneBedroom,
                SUM(CASE WHEN a.apartmentType = com.apartment.domain.apartment.ApartmentType.TWO_BEDROOM        THEN 1L ELSE 0L END) AS twoBedroom,
                SUM(CASE WHEN a.apartmentType = com.apartment.domain.apartment.ApartmentType.TWO_BEDROOM_PLUS   THEN 1L ELSE 0L END) AS twoBedroomPlus,
                SUM(CASE WHEN a.apartmentType = com.apartment.domain.apartment.ApartmentType.THREE_BEDROOM      THEN 1L ELSE 0L END) AS threeBedroom,
                SUM(CASE WHEN a.apartmentType = com.apartment.domain.apartment.ApartmentType.THREE_BEDROOM_PLUS THEN 1L ELSE 0L END) AS threeBedroomPlus,
                SUM(CASE WHEN a.apartmentType = com.apartment.domain.apartment.ApartmentType.PENTHOUSE          THEN 1L ELSE 0L END) AS penthouse,
                SUM(CASE WHEN a.apartmentType = com.apartment.domain.apartment.ApartmentType.DUPLEX             THEN 1L ELSE 0L END) AS duplex,
                SUM(CASE WHEN a.apartmentType = com.apartment.domain.apartment.ApartmentType.OTHER              THEN 1L ELSE 0L END) AS other
            FROM Apartment a
            WHERE (:zoneId IS NULL OR a.building.zone.id = :zoneId)
            """)
    DashboardStatsProjection aggregate(
            @Param("zoneId") UUID zoneId,
            @Param("thisFrom") LocalDateTime thisFrom,
            @Param("thisTo") LocalDateTime thisTo,
            @Param("prevFrom") LocalDateTime prevFrom,
            @Param("prevTo") LocalDateTime prevTo
    );
}
