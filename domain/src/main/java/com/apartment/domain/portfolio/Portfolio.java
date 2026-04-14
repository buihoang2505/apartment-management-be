package com.apartment.domain.portfolio;

import com.apartment.domain.shared.BaseEntity;
import com.apartment.domain.zone.Zone;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "portfolios")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Portfolio extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany
    @JoinTable(
            name = "portfolio_zones",
            joinColumns = @JoinColumn(name = "portfolio_id"),
            inverseJoinColumns = @JoinColumn(name = "zone_id")
    )
    @Builder.Default
    private Set<Zone> zones = new HashSet<>();
}