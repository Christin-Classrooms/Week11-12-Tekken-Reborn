package com.example.battleservice.dto;

public record FighterDTO(
    Long id, 
    String name, 
    int health, 
    double damage, 
    double resistance
) {}
