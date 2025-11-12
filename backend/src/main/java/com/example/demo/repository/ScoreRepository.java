package com.example.demo.repository;

import com.example.demo.model.Score;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * Repository for Score entities backed by H2 (in-memory) via Spring Data JPA.
 *
 * Provides CRUD operations and custom queries for leaderboard and rank calculation.
 */
@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    /**
     * Returns scores ordered for leaderboard: score desc, createdAt asc.
     * Use Pageable to specify limit.
     */
    @Query("SELECT s FROM Score s ORDER BY s.score DESC, s.createdAt ASC")
    List<Score> findLeaderboard(Pageable pageable);

    /**
     * Counts how many scores are strictly greater than the provided score value.
     * Used to compute rank = countGreater + 1.
     */
    @Query("SELECT COUNT(s) FROM Score s WHERE s.score > :score")
    long countByScoreStrictlyGreater(int score);
}
