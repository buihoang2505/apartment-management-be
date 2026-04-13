package com.apartment.app.apartment;

import com.apartment.app.apartment.dto.ApartmentRequest;
import com.apartment.app.apartment.dto.ApartmentResponse;
import com.apartment.domain.apartment.*;
import com.apartment.domain.zone.Building;
import com.apartment.domain.zone.BuildingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final BuildingRepository buildingRepository;

    public Page<ApartmentResponse> findAll(UUID zoneId, UUID buildingId, ApartmentStatus status,
                                           String search, int page, int size) {
        Specification<Apartment> spec = buildSpec(zoneId, buildingId, status, search);
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return apartmentRepository.findAll(spec, pageable).map(ApartmentResponse::from);
    }

    public ApartmentResponse findById(UUID id) {
        return apartmentRepository.findById(id)
                .map(ApartmentResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Căn hộ không tồn tại: " + id));
    }

    @Transactional
    public ApartmentResponse create(ApartmentRequest request) {
        if (apartmentRepository.existsByUnitCode(request.unitCode())) {
            throw new IllegalArgumentException("Mã căn hộ '" + request.unitCode() + "' đã tồn tại");
        }
        Building building = buildingRepository.findById(request.buildingId())
                .orElseThrow(() -> new EntityNotFoundException("Tòa nhà không tồn tại: " + request.buildingId()));

        Apartment apartment = Apartment.builder()
                .unitCode(request.unitCode())
                .displayCode(request.displayCode())
                .area(request.area())
                .sellingPrice(request.sellingPrice())
                .tax(request.tax())
                .status(request.status() != null ? request.status() : ApartmentStatus.DANG_BAN)
                .furnitureDescription(request.furnitureDescription())
                .building(building)
                .build();

        if (request.images() != null) {
            List<ApartmentImage> images = request.images().stream()
                    .map(img -> ApartmentImage.builder()
                            .apartment(apartment)
                            .url(img.url())
                            .label(img.label())
                            .sortOrder(img.sortOrder())
                            .build())
                    .toList();
            apartment.getImages().addAll(images);
        }

        return ApartmentResponse.from(apartmentRepository.save(apartment));
    }

    @Transactional
    public ApartmentResponse update(UUID id, ApartmentRequest request) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Căn hộ không tồn tại: " + id));

        if (!apartment.getUnitCode().equals(request.unitCode())
                && apartmentRepository.existsByUnitCode(request.unitCode())) {
            throw new IllegalArgumentException("Mã căn hộ '" + request.unitCode() + "' đã tồn tại");
        }

        Building building = buildingRepository.findById(request.buildingId())
                .orElseThrow(() -> new EntityNotFoundException("Tòa nhà không tồn tại: " + request.buildingId()));

        apartment.setUnitCode(request.unitCode());
        apartment.setDisplayCode(request.displayCode());
        apartment.setArea(request.area());
        apartment.setSellingPrice(request.sellingPrice());
        apartment.setTax(request.tax());
        if (request.status() != null) apartment.setStatus(request.status());
        apartment.setFurnitureDescription(request.furnitureDescription());
        apartment.setBuilding(building);

        if (request.images() != null) {
            apartment.getImages().clear();
            request.images().stream()
                    .map(img -> ApartmentImage.builder()
                            .apartment(apartment)
                            .url(img.url())
                            .label(img.label())
                            .sortOrder(img.sortOrder())
                            .build())
                    .forEach(apartment.getImages()::add);
        }

        return ApartmentResponse.from(apartmentRepository.save(apartment));
    }

    @Transactional
    public void delete(UUID id) {
        if (!apartmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Căn hộ không tồn tại: " + id);
        }
        apartmentRepository.deleteById(id);
    }

    // ── Specification builder ────────────────────────────────────────────────

    private Specification<Apartment> buildSpec(UUID zoneId, UUID buildingId,
                                               ApartmentStatus status, String search) {
        // Start with fetch spec (never null), then chain conditionally to avoid
        // Specification.and(null) which throws in Spring Data JPA 3+
        Specification<Apartment> spec = fetchAssociations();
        if (zoneId != null)              spec = spec.and(byZone(zoneId));
        if (buildingId != null)          spec = spec.and(byBuilding(buildingId));
        if (status != null)              spec = spec.and(byStatus(status));
        if (StringUtils.hasText(search)) spec = spec.and(bySearch(search));
        return spec;
    }

    /**
     * Eagerly fetch building → zone and images only in data queries (not count queries).
     * Prevents N+1 and avoids Hibernate's HHH90003004 "pagination in memory" warning.
     */
    private Specification<Apartment> fetchAssociations() {
        return (root, query, cb) -> {
            if (Long.class != query.getResultType()) {
                Fetch<?, ?> buildingFetch = root.fetch("building", JoinType.LEFT);
                buildingFetch.fetch("zone", JoinType.LEFT);
                root.fetch("images", JoinType.LEFT);
                query.distinct(true);
            }
            return cb.conjunction();
        };
    }

    private Specification<Apartment> byZone(UUID zoneId) {
        return (root, query, cb) ->
                cb.equal(root.join("building", JoinType.LEFT).join("zone", JoinType.LEFT).get("id"), zoneId);
    }

    private Specification<Apartment> byBuilding(UUID buildingId) {
        return (root, query, cb) ->
                cb.equal(root.join("building", JoinType.LEFT).get("id"), buildingId);
    }

    private Specification<Apartment> byStatus(ApartmentStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    private Specification<Apartment> bySearch(String search) {
        String pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("unitCode")), pattern),
                cb.like(cb.lower(root.get("displayCode")), pattern)
        );
    }
}