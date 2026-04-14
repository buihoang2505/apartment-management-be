package com.apartment.domain.apartment;

import com.apartment.domain.shared.BaseEntity;
import com.apartment.domain.zone.Building;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "apartments")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Apartment extends BaseEntity {

    @Column(name = "unit_code", nullable = false, unique = true)
    private String unitCode;

    @Column(name = "display_code")
    private String displayCode;

    @Column(precision = 10, scale = 2)
    private BigDecimal area;

    @Column(name = "selling_price", precision = 18, scale = 2)
    private BigDecimal sellingPrice;

    @Column(precision = 5, scale = 2)
    private BigDecimal tax;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ApartmentStatus status = ApartmentStatus.DANG_BAN;

    @Column(name = "furniture_description", columnDefinition = "TEXT")
    private String furnitureDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "apartment_type")
    private ApartmentType apartmentType;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "direction")
    private String direction;

    @Column(name = "bedroom_count")
    private Integer bedroomCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    @Builder.Default
    private Set<ApartmentImage> images = new LinkedHashSet<>();
}