package com.apartment.app.portfolio.handler;

import com.apartment.app.portfolio.command.*;
import com.apartment.app.portfolio.dto.PortfolioResponse;
import com.apartment.app.portfolio.exception.PortfolioNotFoundException;
import com.apartment.domain.portfolio.Portfolio;
import com.apartment.domain.portfolio.PortfolioRepository;
import com.apartment.domain.zone.Zone;
import com.apartment.domain.zone.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PortfolioCommandHandler {

    private final PortfolioRepository portfolioRepository;
    private final ZoneRepository zoneRepository;

    public PortfolioResponse handle(CreatePortfolioCommand cmd) {
        Portfolio portfolio = Portfolio.builder()
                .name(cmd.name())
                .description(cmd.description())
                .build();

        if (cmd.zoneIds() != null && !cmd.zoneIds().isEmpty()) {
            List<Zone> zones = zoneRepository.findAllById(cmd.zoneIds());
            portfolio.getZones().addAll(zones);
        }

        return PortfolioResponse.from(portfolioRepository.save(portfolio));
    }

    public PortfolioResponse handle(UpdatePortfolioCommand cmd) {
        Portfolio portfolio = portfolioRepository.findById(cmd.id())
                .orElseThrow(() -> new PortfolioNotFoundException(cmd.id()));

        portfolio.setName(cmd.name());
        portfolio.setDescription(cmd.description());

        portfolio.getZones().clear();
        if (cmd.zoneIds() != null && !cmd.zoneIds().isEmpty()) {
            List<Zone> zones = zoneRepository.findAllById(cmd.zoneIds());
            portfolio.getZones().addAll(zones);
        }

        return PortfolioResponse.from(portfolioRepository.save(portfolio));
    }

    public void handle(DeletePortfolioCommand cmd) {
        if (!portfolioRepository.existsById(cmd.id())) {
            throw new PortfolioNotFoundException(cmd.id());
        }
        portfolioRepository.deleteById(cmd.id());
    }
}