package com.apartment.app.apartment.command;

import java.util.UUID;

public record MoveApartmentCommand(UUID apartmentId, UUID newBuildingId, String note) {}