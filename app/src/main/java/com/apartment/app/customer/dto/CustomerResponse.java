package com.apartment.app.customer.dto;

import com.apartment.domain.customer.Customer;
import com.apartment.domain.customer.CustomerStatus;
import com.apartment.domain.customer.LeadSource;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String fullName,
        String email,
        String phone,
        LeadSource source,
        CustomerStatus status,
        UUID assignedToId,
        String assignedToName,
        UUID interestedApartmentId,
        String interestedApartmentCode,
        String note,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CustomerResponse from(Customer c) {
        return new CustomerResponse(
                c.getId(),
                c.getFullName(),
                c.getEmail(),
                c.getPhone(),
                c.getSource(),
                c.getStatus(),
                c.getAssignedTo() != null ? c.getAssignedTo().getId() : null,
                c.getAssignedTo() != null ? c.getAssignedTo().getFullName() : null,
                c.getInterestedApartment() != null ? c.getInterestedApartment().getId() : null,
                c.getInterestedApartment() != null ? c.getInterestedApartment().getUnitCode() : null,
                c.getNote(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}
