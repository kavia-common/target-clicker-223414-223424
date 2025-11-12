package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * PUBLIC_INTERFACE
 * Global CORS configuration for the backend service.
 *
 * Reads the allowed origins from the "app.cors.allowed-origins" property, which supports a comma-separated list.
 * Defaults to http://localhost:3000 to allow the local React frontend to call this backend on port 3001.
 *
 * This configuration:
 * - Enables CORS for all API paths (/**)
 * - Allows common HTTP methods (GET, POST, PUT, PATCH, DELETE, OPTIONS)
 * - Allows standard headers and credentials
 * - Exposes "Location" and "Link" headers commonly used in REST APIs
 */
@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins:http://localhost:3000}")
    private String allowedOriginsProperty;

    /**
     * PUBLIC_INTERFACE
     * Registers a CorsFilter bean that applies global CORS rules across the application.
     * @return CorsFilter configured with allowed origins, methods, and headers.
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Parse comma-separated origins from property and trim whitespace
        Arrays.stream(allowedOriginsProperty.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(config::addAllowedOrigin);

        // Allow credentials (cookies/authorization headers)
        config.setAllowCredentials(true);

        // Allowed methods including OPTIONS for preflight
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Common headers used by browsers and APIs - broaden to reduce preflights
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Cache-Control",
                "Content-Type",
                "X-Requested-With",
                "Origin",
                "Accept",
                "Accept-Language",
                "Accept-Encoding",
                "DNT",
                "If-Modified-Since",
                "Keep-Alive",
                "User-Agent",
                "X-CSRF-Token",
                "X-HTTP-Method-Override"
        ));

        // Expose some response headers to the browser if needed
        config.setExposedHeaders(Arrays.asList(
                "Location",
                "Link",
                "X-Total-Count"
        ));

        // Apply to all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
