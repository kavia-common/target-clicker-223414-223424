package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PUBLIC_INTERFACE
 * Entry point for the Spring Boot backend application.
 *
 * This application uses component scanning rooted at 'com.example.demo'
 * to discover controllers, repositories, entities, and other components
 * under the same base package. Repositories in com.example.demo.repository
 * are auto-detected by Spring Data JPA.
 */
@SpringBootApplication
public class BackendApplication {

    /**
     * PUBLIC_INTERFACE
     * Bootstraps the Spring application.
     * @param args CLI args
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
