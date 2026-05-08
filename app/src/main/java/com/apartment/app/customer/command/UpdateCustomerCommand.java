package com.apartment.app.customer.command;

import com.apartment.domain.customer.CustomerStatus;
import com.apartment.domain.customer.LeadSource;

import java.util.UUID;

public record UpdateCustomerCommand(
        UUID id,
        String fullName,
        String email,
        String phone,
        LeadSource source,
        CustomerStatus status,
        UUID assignedToId,
        UUID interestedApartmentId,
        String note
) {}
