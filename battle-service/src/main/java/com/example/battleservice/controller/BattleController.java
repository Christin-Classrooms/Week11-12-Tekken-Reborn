package com.example.battleservice.controller;

import com.example.battleservice.dto.BattleRequest;
import com.example.battleservice.entity.Battle;
import com.example.battleservice.service.BattleService;
import com.example.battleservice.repository.BattleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/battles")
public class BattleController {

    private final BattleService battleService;
    private final BattleRepository battleRepository;

    public BattleController(BattleService battleService, BattleRepository battleRepository) {
        this.battleService = battleService;
        this.battleRepository = battleRepository;
    }

    @PostMapping
    public ResponseEntity<Battle> createBattle(@RequestBody BattleRequest request) {
        Battle savedBattle = battleService.simulate(request);
        return new ResponseEntity<>(savedBattle, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Battle> getAllBattles() {
        return battleRepository.findAll();
    }

    @GetMapping("/player/{playerId}")
    public List<Battle> getBattlesByPlayer(@PathVariable Long playerId) {
        return battleRepository.findByPlayerId(playerId);
    }
}
