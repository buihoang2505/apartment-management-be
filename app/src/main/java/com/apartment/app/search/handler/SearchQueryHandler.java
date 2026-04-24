package com.apartment.app.search.handler;

import com.apartment.app.search.dto.SearchResponse;
import com.apartment.app.search.dto.SearchResponse.ApartmentSearchItem;
import com.apartment.app.search.dto.SearchResponse.EmployeeSearchItem;
import com.apartment.app.search.dto.SearchResponse.PortfolioSearchItem;
import com.apartment.domain.apartment.ApartmentRepository;
import com.apartment.domain.employee.EmployeeRepository;
import com.apartment.domain.portfolio.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchQueryHandler {

    private static final int MAX_RESULTS = 5;

    private final ApartmentRepository apartmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PortfolioRepository portfolioRepository;

    public SearchResponse search(String q) {
        if (q == null || q.trim().length() < 2) {
            return new SearchResponse(List.of(), List.of(), List.of());
        }

        var pageable = PageRequest.of(0, MAX_RESULTS);
        var keyword = q.trim();

        var apartments = apartmentRepository.searchByKeyword(keyword, pageable).stream()
                .map(a -> new ApartmentSearchItem(
                        a.getId().toString(),
                        a.getUnitCode(),
                        a.getDisplayCode(),
                        a.getStatus() != null ? a.getStatus().name() : null,
                        a.getApartmentType() != null ? a.getApartmentType().name() : null
                )).toList();

        var employees = employeeRepository.searchByKeyword(keyword, pageable).stream()
                .map(e -> new EmployeeSearchItem(
                        e.getId().toString(),
                        e.getFullName(),
                        e.getEmail(),
                        e.getPosition(),
                        e.getDepartment() != null ? e.getDepartment().getName() : null
                )).toList();

        var portfolios = portfolioRepository.searchByKeyword(keyword, pageable).stream()
                .map(p -> new PortfolioSearchItem(
                        p.getId().toString(),
                        p.getName(),
                        p.getDescription(),
                        p.getZones() != null ? p.getZones().size() : 0
                )).toList();

        log.debug("[Search] q='{}' → apartments={}, employees={}, portfolios={}",
                keyword, apartments.size(), employees.size(), portfolios.size());

        return new SearchResponse(apartments, employees, portfolios);
    }
}
