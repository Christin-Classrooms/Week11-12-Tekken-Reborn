package com.example.battleservice.repository;

import com.example.battleservice.entity.Battle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BattleRepository extends JpaRepository<Battle, Long> {
    List<Battle> findByPlayerId(Long playerId);
}
