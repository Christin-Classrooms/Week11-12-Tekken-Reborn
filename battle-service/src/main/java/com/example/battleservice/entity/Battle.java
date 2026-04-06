package com.example.battleservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "battles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fighter1Id;
    private Long fighter2Id;
    private String fighter1Name;
    private String fighter2Name;
    private Long winnerId;
    private String winnerName;
    private String loserName;
    private Long playerId;
    private String playerName;

    @Column(columnDefinition = "TEXT")
    private String battleLog;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
