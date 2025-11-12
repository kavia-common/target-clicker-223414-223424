package com.example.demo.controller;

import com.example.demo.dto.ScoreRequest;
import com.example.demo.dto.ScoreResponse;
import com.example.demo.model.Score;
import com.example.demo.repository.ScoreRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * PUBLIC_INTERFACE
 * GameController provides endpoints for score submission and leaderboard retrieval.
 *
 * Routes:
 * - POST /api/scores: Accepts validated ScoreRequest, persists Score, returns ScoreResponse including computed rank.
 * - GET /api/leaderboard?limit=10: Returns top scores ordered by score desc then createdAt asc.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Game", description = "Endpoints for posting scores and retrieving leaderboard")
public class GameController {

    private final ScoreRepository scoreRepository;

    public GameController(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    /**
     * PUBLIC_INTERFACE
     * Submit a new score. Validates input and persists a Score entity.
     *
     * @param request The score submission payload with playerName, score, durationMs.
     * @return ScoreResponse including id, playerName, score, durationMs, createdAt, and computed rank.
     */
    @PostMapping(value = "/scores", consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Submit a score",
            description = "Accepts a score payload, validates it, persists, and returns the created score with rank. " +
                    "Rank is computed as the number of scores strictly greater than this score plus one.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Score created",
                            content = @Content(schema = @Schema(implementation = ScoreResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error",
                            content = @Content(schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "500", description = "Server error")
            }
    )
    public ResponseEntity<ScoreResponse> createScore(
            @Valid @RequestBody ScoreRequest request
    ) {
        // Map DTO to entity
        Score score = new Score();
        score.setPlayerName(request.getPlayerName().trim());
        score.setScore(request.getScore());
        score.setDurationMs((long) request.getDurationMs());
        score.setCreatedAt(Instant.now());

        // Persist
        Score saved;
        try {
            saved = scoreRepository.save(score);
        } catch (DataAccessException dae) {
            // Bubble up as 500 with minimal message
            throw dae;
        }

        // Compute rank: count(scores with score > saved.score) + 1
        long countGreater = scoreRepository.countByScoreStrictlyGreater(saved.getScore());
        long rank = countGreater + 1;

        ScoreResponse response = new ScoreResponse(
                saved.getId(),
                saved.getPlayerName(),
                saved.getScore(),
                saved.getDurationMs(),
                saved.getCreatedAt(),
                rank
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUBLIC_INTERFACE
     * Retrieve the leaderboard with a limit. Orders by score desc then createdAt asc.
     *
     * @param limit Optional query param, default 10, max 50.
     * @return A list of ScoreResponse without rank computation (rank is not strictly required for leaderboard items,
     * but we can compute relative rank based on position if desired; here we compute absolute rank for clarity).
     */
    @GetMapping(value = "/leaderboard", produces = "application/json")
    @Operation(
            summary = "Get leaderboard",
            description = "Returns top scores ordered by score (desc) and createdAt (asc). Limit defaults to 10 and is capped at 50.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leaderboard entries",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ScoreResponse.class)))),
                    @ApiResponse(responseCode = "400", description = "Invalid limit",
                            content = @Content(schema = @Schema(implementation = Map.class)))
            }
    )
    public ResponseEntity<List<ScoreResponse>> getLeaderboard(
            @RequestParam(name = "limit", required = false)
            @Parameter(description = "Maximum number of results to return (default 10, max 50)", example = "10")
            Integer limit
    ) {
        int effectiveLimit = 10;
        if (limit != null) {
            if (limit < 1) {
                return ResponseEntity.badRequest().build();
            }
            effectiveLimit = Math.min(limit, 50);
        }

        Pageable pageable = PageRequest.of(0, effectiveLimit);
        List<Score> entries = scoreRepository.findLeaderboard(pageable);

        // Compute rank per entry using count of strictly greater scores
        List<ScoreResponse> responses = entries.stream().map(s -> {
            long rank = scoreRepository.countByScoreStrictlyGreater(s.getScore()) + 1;
            return new ScoreResponse(
                    s.getId(),
                    s.getPlayerName(),
                    s.getScore(),
                    s.getDurationMs(),
                    s.getCreatedAt(),
                    rank
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * PUBLIC_INTERFACE
     * Global handler for validation errors on @Valid payloads.
     *
     * @param ex MethodArgumentNotValidException thrown by Spring on validation failure.
     * @return A map of field -> error message and HTTP 400 status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Operation(
            summary = "Validation error handler",
            description = "Returns a map of field errors when request validation fails."
    )
    public Map<String, Object> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : result.getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Validation failed");
        response.put("errors", fieldErrors);
        return response;
    }
}
