package com.apartment.app.portfolio.handler;

import com.apartment.app.portfolio.dto.PortfolioResponse;
import com.apartment.app.portfolio.exception.PortfolioNotFoundException;
import com.apartment.domain.portfolio.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioQueryHandler {

    private final PortfolioRepository portfolioRepository;

    public List<PortfolioResponse> findAll() {
        return portfolioRepository.findAll().stream()
                .map(PortfolioResponse::from)
                .toList();
    }

    public PortfolioResponse findById(UUID id) {
        return portfolioRepository.findById(id)
                .map(PortfolioResponse::from)
                .orElseThrow(() -> new PortfolioNotFoundException(id));
    }
}