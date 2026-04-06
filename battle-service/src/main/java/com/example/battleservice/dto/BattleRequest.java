package com.example.battleservice.dto;

public record BattleRequest(
    Long fighter1Id, 
    Long fighter2Id, 
    Long playerId, 
    String playerName
) {}
