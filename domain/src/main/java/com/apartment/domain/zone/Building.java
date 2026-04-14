package com.apartment.domain.zone;

import com.apartment.domain.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "buildings", uniqueConstraints = @UniqueConstraint(columnNames = {"zone_id", "code"}))
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Building extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BuildingType type;

    @Column(name = "total_floors")
    private Integer totalFloors;

    @Column(columnDefinition = "TEXT")
    private String description;
}