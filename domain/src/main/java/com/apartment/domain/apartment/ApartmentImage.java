package com.apartment.domain.apartment;

import com.apartment.domain.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "apartment_images")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ApartmentImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String url;

    private String label;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
}