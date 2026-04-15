package com.apartment.app.zone.command;

import java.util.UUID;

public record UpdateZoneCommand(UUID id, String name, String code, String description) {}