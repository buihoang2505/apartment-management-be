package com.apartment.interfaces.contract.request;

import com.apartment.domain.contract.ContractStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateContractStatusRequest(@NotNull ContractStatus status) {}
