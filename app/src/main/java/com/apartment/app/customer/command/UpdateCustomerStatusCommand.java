package com.apartment.app.customer.command;

import com.apartment.domain.customer.CustomerStatus;

import java.util.UUID;

public record UpdateCustomerStatusCommand(UUID id, CustomerStatus status) {}
