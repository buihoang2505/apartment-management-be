package com.apartment.app.apartment.handler;

import com.apartment.app.apartment.dto.ApartmentResponse;
import com.apartment.app.apartment.dto.ApartmentStatusHistoryResponse;
import com.apartment.app.apartment.exception.ApartmentNotFoundException;
import com.apartment.domain.apartment.*;
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
public class ApartmentQueryHandler {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentStatusHistoryRepository statusHistoryRepository;

    public Page<ApartmentResponse> findAll(UUID zoneId, UUID buildingId, ApartmentStatus status,
                                           ApartmentType type, String search, int page, int size) {
        Specification<Apartment> spec = buildSpec(zoneId, buildingId, status, type, search);
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return apartmentRepository.findAll(spec, pageable).map(ApartmentResponse::from);
    }

    public ApartmentResponse findById(UUID id) {
        return apartmentRepository.findById(id)
                .map(ApartmentResponse::from)
                .orElseThrow(() -> new ApartmentNotFoundException(id));
    }

    public List<ApartmentStatusHistoryResponse> findStatusHistory(UUID id) {
        if (!apartmentRepository.existsById(id)) {
            throw new ApartmentNotFoundException(id);
        }
        return statusHistoryRepository.findByApartmentIdOrderByCreatedAtDesc(id)
                .stream()
                .map(ApartmentStatusHistoryResponse::from)
                .toList();
    }

    // ── Specification builder ────────────────────────────────────────────────

    private Specification<Apartment> buildSpec(UUID zoneId, UUID buildingId,
                                               ApartmentStatus status, ApartmentType type,
                                               String search) {
        Specification<Apartment> spec = fetchAssociations();
        if (zoneId != null)              spec = spec.and(byZone(zoneId));
        if (buildingId != null)          spec = spec.and(byBuilding(buildingId));
        if (status != null)              spec = spec.and(byStatus(status));
        if (type != null)                spec = spec.and(byType(type));
        if (StringUtils.hasText(search)) spec = spec.and(bySearch(search));
        return spec;
    }

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

    private Specification<Apartment> byType(ApartmentType type) {
        return (root, query, cb) -> cb.equal(root.get("apartmentType"), type);
    }

    private Specification<Apartment> bySearch(String search) {
        String pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("unitCode")), pattern),
                cb.like(cb.lower(root.get("displayCode")), pattern)
        );
    }
}