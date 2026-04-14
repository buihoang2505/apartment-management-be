package com.apartment.app.portfolio.command;

import java.util.List;
import java.util.UUID;

public record CreatePortfolioCommand(String name, String description, List<UUID> zoneIds) {}