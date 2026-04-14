package com.apartment.app.portfolio.exception;

import java.util.UUID;

public class PortfolioNotFoundException extends RuntimeException {
    public PortfolioNotFoundException(UUID id) {
        super("Danh mục không tồn tại: " + id);
    }
}