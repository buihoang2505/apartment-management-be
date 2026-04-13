package com.apartment.infra.config;

import org.springframework.context.annotation.Configuration;

/**
 * CORS is configured centrally in SecurityConfig.corsConfigurationSource()
 * so that Spring Security's filter chain handles preflight requests correctly.
 */
@Configuration
public class WebConfig {
}