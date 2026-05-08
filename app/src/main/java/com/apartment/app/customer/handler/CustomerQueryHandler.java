package com.apartment.app.customer.handler;

import com.apartment.app.customer.dto.CustomerResponse;
import com.apartment.app.customer.dto.CustomerStatsResponse;
import com.apartment.app.customer.exception.CustomerNotFoundException;
import com.apartment.domain.customer.CustomerRepository;
import com.apartment.domain.customer.CustomerStatus;
import com.apartment.domain.customer.LeadSource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerQueryHandler {

    private final CustomerRepository customerRepository;

    public Page<CustomerResponse> findAll(
            String keyword,
            CustomerStatus status,
            LeadSource source,
            UUID assignedToId,
            Pageable pageable) {
        String kw = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;
        return customerRepository.search(kw, status, source, assignedToId, pageable)
                .map(CustomerResponse::from);
    }

    public CustomerResponse findById(UUID id) {
        return customerRepository.findById(id)
                .map(CustomerResponse::from)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public CustomerStatsResponse getStats() {
        Map<CustomerStatus, Long> byStatus = new EnumMap<>(CustomerStatus.class);
        for (CustomerStatus s : CustomerStatus.values()) byStatus.put(s, 0L);
        long total = 0;
        for (Object[] row : customerRepository.countGroupByStatus()) {
            CustomerStatus s = (CustomerStatus) row[0];
            long count = (Long) row[1];
            byStatus.put(s, count);
            total += count;
        }
        return new CustomerStatsResponse(total, byStatus);
    }
}
