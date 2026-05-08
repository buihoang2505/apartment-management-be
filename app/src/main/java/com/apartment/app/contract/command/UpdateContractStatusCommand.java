package com.apartment.app.contract.command;

import com.apartment.domain.contract.ContractStatus;

import java.util.UUID;

public record UpdateContractStatusCommand(UUID id, ContractStatus status) {}
