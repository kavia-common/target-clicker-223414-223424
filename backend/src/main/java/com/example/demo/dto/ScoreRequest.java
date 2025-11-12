package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * PUBLIC_INTERFACE
 * Data Transfer Object for creating a new score.
 * Includes Jakarta Bean Validation annotations to ensure constraints:
 * - playerName: 1-20 characters, non-blank
 * - score: >= 0
 * - durationMs: >= 0 and <= 300000 (5 minutes)
 */
@Schema(description = "Request payload for submitting a player's score")
public class ScoreRequest {

    @NotBlank(message = "playerName must not be blank")
    @Size(min = 1, max = 20, message = "playerName must be between 1 and 20 characters")
    @Schema(description = "Player display name", example = "Alice", minLength = 1, maxLength = 20, requiredMode = Schema.RequiredMode.REQUIRED)
    private String playerName;

    @Min(value = 0, message = "score must be >= 0")
    @Schema(description = "Score points achieved", example = "123", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private int score;

    @Min(value = 0, message = "durationMs must be >= 0")
    @Max(value = 300000, message = "durationMs must be <= 300000")
    @Schema(description = "Duration of the game session in milliseconds (max 300000 = 5 minutes)",
            example = "30000", minimum = "0", maximum = "300000", requiredMode = Schema.RequiredMode.REQUIRED)
    private int durationMs;

    /** PUBLIC_INTERFACE */
    public ScoreRequest() {
        // Default constructor for deserialization
    }

    /** PUBLIC_INTERFACE */
    public String getPlayerName() {
        /** Returns the player's name. */
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /** PUBLIC_INTERFACE */
    public int getScore() {
        /** Returns the score value. */
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /** PUBLIC_INTERFACE */
    public int getDurationMs() {
        /** Returns the session duration in milliseconds. */
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }
}
