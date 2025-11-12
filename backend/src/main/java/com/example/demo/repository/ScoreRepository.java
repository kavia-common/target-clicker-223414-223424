package com.example.demo.repository;

import com.example.demo.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PUBLIC_INTERFACE
 * Repository for Score entities backed by H2 (in-memory) via Spring Data JPA.
 *
 * Provides CRUD operations and can be extended with custom queries as needed.
 */
@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    // Additional query methods can be defined here, e.g.:
    // List<Score> findTop10ByOrderByScoreDesc();
}
