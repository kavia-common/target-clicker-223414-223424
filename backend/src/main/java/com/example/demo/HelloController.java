package com.example.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * PUBLIC_INTERFACE
 * Basic controller providing welcome, health, and documentation redirect endpoints.
 */
@RestController
@Tag(name = "Hello Controller", description = "Basic endpoints for backend")
public class HelloController {

    /**
     * PUBLIC_INTERFACE
     * Root welcome endpoint.
     * @return A welcome message string.
     */
    @GetMapping("/")
    @Operation(summary = "Welcome endpoint", description = "Returns a welcome message")
    public String hello() {
        return "Hello, Spring Boot! Welcome to backend";
    }

    /**
     * PUBLIC_INTERFACE
     * Redirects to Swagger UI, preserving original request scheme/host/port (including proxy headers).
     * @param request HttpServletRequest from client
     * @return RedirectView to swagger-ui.html
     */
    @GetMapping("/docs")
    @Operation(summary = "API Documentation", description = "Redirects to Swagger UI preserving original scheme/host/port")
    public RedirectView docs(HttpServletRequest request) {
        String target = UriComponentsBuilder
                .fromHttpRequest(new ServletServerHttpRequest(request))
                .replacePath("/swagger-ui.html")
                .replaceQuery(null)
                .build()
                .toUriString();

        RedirectView rv = new RedirectView(target);
        rv.setHttp10Compatible(false);
        return rv;
    }

    /**
     * PUBLIC_INTERFACE
     * Simple health check.
     * @return "OK" when service is up.
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Returns application health status")
    public String health() {
        return "OK";
    }

    /**
     * PUBLIC_INTERFACE
     * Application information endpoint.
     * @return Basic application info string.
     */
    @GetMapping("/api/info")
    @Operation(summary = "Application info", description = "Returns application information")
    public String info() {
        return "Spring Boot Application: backend";
    }
}