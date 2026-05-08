package com.apartment.app.contract.handler;

import com.apartment.app.contract.dto.ContractResponse;
import com.apartment.app.contract.dto.ContractStatsResponse;
import com.apartment.app.contract.exception.ContractNotFoundException;
import com.apartment.domain.contract.ContractRepository;
import com.apartment.domain.contract.ContractStatus;
import com.apartment.domain.contract.ContractType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractQueryHandler {

    private final ContractRepository contractRepository;

    @Transactional(readOnly = true)
    public Page<ContractResponse> findAll(String keyword, ContractType type, ContractStatus status, Pageable pageable) {
        return contractRepository.search(
                keyword == null || keyword.isBlank() ? null : keyword,
                type, status, pageable
        ).map(c -> ContractResponse.from(c, false));
    }

    @Transactional(readOnly = true)
    public ContractResponse findById(UUID id) {
        return contractRepository.findWithSchedulesById(id)
                .map(c -> ContractResponse.from(c, true))
                .orElseThrow(() -> new ContractNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public ContractStatsResponse getStats() {
        Map<ContractStatus, Long> byStatus = new EnumMap<>(ContractStatus.class);
        for (var s : ContractStatus.values()) byStatus.put(s, 0L);
        long total = 0;
        for (Object[] row : contractRepository.countGroupByStatus()) {
            ContractStatus st = (ContractStatus) row[0];
            long cnt = (Long) row[1];
            byStatus.put(st, cnt);
            total += cnt;
        }
        BigDecimal outstanding = contractRepository.sumOutstanding();
        BigDecimal overdue = contractRepository.sumOverdue();
        return new ContractStatsResponse(total, byStatus, outstanding, overdue);
    }
}
