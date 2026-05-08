package com.apartment.app.contract.command;

import java.util.UUID;

public record DeletePaymentScheduleCommand(UUID scheduleId) {}
