package com.example.demo.repository;

import com.example.demo.model.Score;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ScoreRepositoryTest {

    @Autowired
    private ScoreRepository scoreRepository;

    @Test
    void save_and_find_leaderboard_ordering() {
        scoreRepository.save(new Score("A", 10, 30000L, Instant.now().minusSeconds(60)));
        scoreRepository.save(new Score("B", 20, 30000L, Instant.now().minusSeconds(50)));
        scoreRepository.save(new Score("C", 20, 30000L, Instant.now().minusSeconds(40)));
        scoreRepository.save(new Score("D", 5, 30000L, Instant.now().minusSeconds(30)));

        List<Score> top = scoreRepository.findLeaderboard(PageRequest.of(0, 10));
        assertThat(top).hasSize(4);
        assertThat(top.get(0).getPlayerName()).isEqualTo("B");
        assertThat(top.get(1).getPlayerName()).isEqualTo("C");
        assertThat(top.get(2).getPlayerName()).isEqualTo("A");
        assertThat(top.get(3).getPlayerName()).isEqualTo("D");
    }

    @Test
    void countByScoreStrictlyGreater_computes_rank_base() {
        scoreRepository.save(new Score("A", 10, 30000L, Instant.now()));
        scoreRepository.save(new Score("B", 20, 30000L, Instant.now()));
        scoreRepository.save(new Score("C", 20, 30000L, Instant.now()));

        long gt10 = scoreRepository.countByScoreStrictlyGreater(10);
        long gt20 = scoreRepository.countByScoreStrictlyGreater(20);
        long gt0 = scoreRepository.countByScoreStrictlyGreater(0);

        assertThat(gt10).isEqualTo(2); // B, C > 10
        assertThat(gt20).isEqualTo(0);
        assertThat(gt0).isEqualTo(3);
    }
}
