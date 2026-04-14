package com.apartment.app.apartment.handler;

import com.apartment.app.apartment.command.*;
import com.apartment.app.apartment.dto.ApartmentResponse;
import com.apartment.app.apartment.exception.ApartmentNotFoundException;
import com.apartment.app.apartment.exception.DuplicateUnitCodeException;
import com.apartment.app.zone.exception.BuildingNotFoundException;
import com.apartment.domain.apartment.*;
import com.apartment.domain.user.User;
import com.apartment.domain.user.UserRepository;
import com.apartment.domain.zone.Building;
import com.apartment.domain.zone.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ApartmentCommandHandler {

    private final ApartmentRepository apartmentRepository;
    private final BuildingRepository buildingRepository;
    private final ApartmentStatusHistoryRepository statusHistoryRepository;
    private final UserRepository userRepository;

    public ApartmentResponse handle(CreateApartmentCommand cmd) {
        if (apartmentRepository.existsByUnitCode(cmd.unitCode())) {
            throw new DuplicateUnitCodeException(cmd.unitCode());
        }
        Building building = buildingRepository.findById(cmd.buildingId())
                .orElseThrow(() -> new BuildingNotFoundException(cmd.buildingId()));

        Apartment apartment = Apartment.builder()
                .unitCode(cmd.unitCode())
                .displayCode(cmd.displayCode())
                .area(cmd.area())
                .sellingPrice(cmd.sellingPrice())
                .tax(cmd.tax())
                .status(cmd.status() != null ? cmd.status() : ApartmentStatus.DANG_BAN)
                .furnitureDescription(cmd.furnitureDescription())
                .apartmentType(cmd.apartmentType())
                .floorNumber(cmd.floorNumber())
                .direction(cmd.direction())
                .bedroomCount(cmd.bedroomCount())
                .building(building)
                .build();

        if (cmd.images() != null) {
            Set<ApartmentImage> images = new LinkedHashSet<>();
            cmd.images().forEach(img -> images.add(ApartmentImage.builder()
                    .apartment(apartment)
                    .url(img.url())
                    .label(img.label())
                    .sortOrder(img.sortOrder())
                    .build()));
            apartment.getImages().addAll(images);
        }

        Apartment saved = apartmentRepository.save(apartment);
        recordHistory(saved, null, saved.getStatus(), "Tạo mới căn hộ");
        return ApartmentResponse.from(saved);
    }

    public ApartmentResponse handle(UpdateApartmentCommand cmd) {
        Apartment apartment = apartmentRepository.findById(cmd.id())
                .orElseThrow(() -> new ApartmentNotFoundException(cmd.id()));

        if (!apartment.getUnitCode().equals(cmd.unitCode())
                && apartmentRepository.existsByUnitCode(cmd.unitCode())) {
            throw new DuplicateUnitCodeException(cmd.unitCode());
        }

        Building building = buildingRepository.findById(cmd.buildingId())
                .orElseThrow(() -> new BuildingNotFoundException(cmd.buildingId()));

        ApartmentStatus oldStatus = apartment.getStatus();

        apartment.setUnitCode(cmd.unitCode());
        apartment.setDisplayCode(cmd.displayCode());
        apartment.setArea(cmd.area());
        apartment.setSellingPrice(cmd.sellingPrice());
        apartment.setTax(cmd.tax());
        if (cmd.status() != null) apartment.setStatus(cmd.status());
        apartment.setFurnitureDescription(cmd.furnitureDescription());
        apartment.setApartmentType(cmd.apartmentType());
        apartment.setFloorNumber(cmd.floorNumber());
        apartment.setDirection(cmd.direction());
        apartment.setBedroomCount(cmd.bedroomCount());
        apartment.setBuilding(building);

        if (cmd.images() != null) {
            apartment.getImages().clear();
            cmd.images().stream()
                    .map(img -> ApartmentImage.builder()
                            .apartment(apartment)
                            .url(img.url())
                            .label(img.label())
                            .sortOrder(img.sortOrder())
                            .build())
                    .forEach(apartment.getImages()::add);
        }

        Apartment saved = apartmentRepository.save(apartment);

        if (cmd.status() != null && !cmd.status().equals(oldStatus)) {
            recordHistory(saved, oldStatus, saved.getStatus(), cmd.statusNote());
        }

        return ApartmentResponse.from(saved);
    }

    public void handle(DeleteApartmentCommand cmd) {
        if (!apartmentRepository.existsById(cmd.id())) {
            throw new ApartmentNotFoundException(cmd.id());
        }
        apartmentRepository.deleteById(cmd.id());
    }

    public ApartmentResponse handle(MoveApartmentCommand cmd) {
        Apartment apartment = apartmentRepository.findById(cmd.apartmentId())
                .orElseThrow(() -> new ApartmentNotFoundException(cmd.apartmentId()));
        Building newBuilding = buildingRepository.findById(cmd.newBuildingId())
                .orElseThrow(() -> new BuildingNotFoundException(cmd.newBuildingId()));
        apartment.setBuilding(newBuilding);
        return ApartmentResponse.from(apartmentRepository.save(apartment));
    }

    // ── helpers ─────────────────────────────────────────────────────────────

    private void recordHistory(Apartment apartment, ApartmentStatus oldStatus,
                               ApartmentStatus newStatus, String note) {
        ApartmentStatusHistory history = ApartmentStatusHistory.builder()
                .apartment(apartment)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedBy(getCurrentUser())
                .note(note)
                .build();
        statusHistoryRepository.save(history);
    }

    private User getCurrentUser() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return userRepository.findByUsername(username).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}