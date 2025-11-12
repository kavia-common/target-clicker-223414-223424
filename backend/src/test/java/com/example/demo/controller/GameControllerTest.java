package com.example.demo.controller;

import com.example.demo.dto.ScoreRequest;
import com.example.demo.model.Score;
import com.example.demo.repository.ScoreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        scoreRepository.deleteAll();
    }

    @Test
    void post_scores_happy_path() throws Exception {
        ScoreRequest req = new ScoreRequest();
        req.setPlayerName("Alice");
        req.setScore(42);
        req.setDurationMs(30000);

        mockMvc.perform(post("/api/scores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.playerName", is("Alice")))
                .andExpect(jsonPath("$.score", is(42)))
                .andExpect(jsonPath("$.durationMs", is(30000)))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.rank", is(1)));
    }

    @Test
    void post_scores_validation_errors() throws Exception {
        // Blank name
        ScoreRequest req1 = new ScoreRequest();
        req1.setPlayerName(" ");
        req1.setScore(5);
        req1.setDurationMs(1000);

        mockMvc.perform(post("/api/scores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req1)))
                .andExpect(status().isBadRequest());

        // Negative score
        ScoreRequest req2 = new ScoreRequest();
        req2.setPlayerName("Bob");
        req2.setScore(-1);
        req2.setDurationMs(1000);

        mockMvc.perform(post("/api/scores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req2)))
                .andExpect(status().isBadRequest());

        // duration out of range
        ScoreRequest req3 = new ScoreRequest();
        req3.setPlayerName("Carl");
        req3.setScore(1);
        req3.setDurationMs(400000);

        mockMvc.perform(post("/api/scores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req3)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_leaderboard_order_and_limit() throws Exception {
        // Create entries with deliberate scores and timestamps
        scoreRepository.save(new Score("A", 10, 30000L, Instant.now().minusSeconds(60)));
        scoreRepository.save(new Score("B", 20, 30000L, Instant.now().minusSeconds(50)));
        scoreRepository.save(new Score("C", 20, 30000L, Instant.now().minusSeconds(40))); // same score, later time
        scoreRepository.save(new Score("D", 5, 30000L, Instant.now().minusSeconds(30)));

        // Leaderboard should be score desc then createdAt asc => B (older), C (newer), A, D
        mockMvc.perform(get("/api/leaderboard").param("limit", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].playerName", is("B")))
                .andExpect(jsonPath("$[1].playerName", is("C")))
                .andExpect(jsonPath("$[2].playerName", is("A")))
                .andExpect(jsonPath("$[0].rank", is(1)))
                .andExpect(jsonPath("$[1].rank", is(1))) // same score gets same computed rank base (count strictly greater + 1)
                .andExpect(jsonPath("$[2].rank", is(3)));
    }
}
