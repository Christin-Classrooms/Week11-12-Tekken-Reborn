package com.example.battleservice.service;

import com.example.battleservice.dto.BattleRequest;
import com.example.battleservice.dto.FighterDTO;
import com.example.battleservice.entity.Battle;
import com.example.battleservice.repository.BattleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service to simulate battles between fighters.
 * Communicates with main-app via RestTemplate and saves results to DB.
 */
@Service
public class BattleService {

    private final BattleRepository battleRepository;
    private final RestTemplate restTemplate;

    @Value("${FIGHTER_SERVICE_URL:http://app:8080/api/fighters}")
    private String fighterServiceUrl;

    public BattleService(BattleRepository battleRepository, RestTemplate restTemplate) {
        this.battleRepository = battleRepository;
        this.restTemplate = restTemplate;
    }

    public Battle simulate(BattleRequest request) {
        // 1. Call GET http://main-app:8080/api/fighters/{id}
        FighterDTO f1 = fetchFighter(request.fighter1Id());
        FighterDTO f2 = fetchFighter(request.fighter2Id());

        if (f1 == null || f2 == null) {
            throw new RuntimeException("One or both fighters could not be found.");
        }

        // 2. Simulate the fight logic as requested
        double score1 = f1.health() + (f1.damage() * 10) + (f1.resistance() * 50);
        double score2 = f2.health() + (f2.damage() * 10) + (f2.resistance() * 50);

        FighterDTO winner = (score1 >= score2) ? f1 : f2;
        FighterDTO loser = (winner == f1) ? f2 : f1;

        String battleLog = String.format("%s (power: %.1f) vs %s (power: %.1f) — %s wins by %.1f points!",
                f1.name(), score1, f2.name(), score2, winner.name(), Math.abs(score1 - score2));

        // 3. Build and save valid Battle entity
        Battle battle = Battle.builder()
                .fighter1Id(f1.id())
                .fighter2Id(f2.id())
                .fighter1Name(f1.name())
                .fighter2Name(f2.name())
                .winnerId(winner.id())
                .winnerName(winner.name())
                .loserName(loser.name())
                .playerId(request.playerId())
                .playerName(request.playerName())
                .battleLog(battleLog)
                .build();

        return battleRepository.save(battle);
    }

    private FighterDTO fetchFighter(Long id) {
        try {
            String url = fighterServiceUrl + "/" + id;
            return restTemplate.getForObject(url, FighterDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching fighter with ID " + id + ": " + e.getMessage());
        }
    }
}




























