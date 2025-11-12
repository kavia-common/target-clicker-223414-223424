package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * PUBLIC_INTERFACE
 * Response DTO representing a score entry returned by the API.
 */
@Schema(description = "Response object for a persisted score including rank")
public class ScoreResponse {

    @Schema(description = "Unique identifier for the score", example = "1")
    private Long id;

    @Schema(description = "Player display name", example = "Alice")
    private String playerName;

    @Schema(description = "Score points achieved", example = "123")
    private int score;

    @Schema(description = "Duration of the game session in milliseconds", example = "30000")
    private long durationMs;

    @Schema(description = "Creation timestamp (UTC)", example = "2025-01-01T12:34:56.789Z")
    private Instant createdAt;

    @Schema(description = "Rank based on count of strictly greater scores + 1", example = "42")
    private long rank;

    /** PUBLIC_INTERFACE */
    public ScoreResponse() {
        // Default
    }

    /** PUBLIC_INTERFACE */
    public ScoreResponse(Long id, String playerName, int score, long durationMs, Instant createdAt, long rank) {
        this.id = id;
        this.playerName = playerName;
        this.score = score;
        this.durationMs = durationMs;
        this.createdAt = createdAt;
        this.rank = rank;
    }

    /** PUBLIC_INTERFACE */
    public Long getId() {
        /** Returns ID */
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** PUBLIC_INTERFACE */
    public String getPlayerName() {
        /** Returns player name */
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /** PUBLIC_INTERFACE */
    public int getScore() {
        /** Returns score */
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /** PUBLIC_INTERFACE */
    public long getDurationMs() {
        /** Returns duration in ms */
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    /** PUBLIC_INTERFACE */
    public Instant getCreatedAt() {
        /** Returns creation timestamp */
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /** PUBLIC_INTERFACE */
    public long getRank() {
        /** Returns rank position */
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }
}
