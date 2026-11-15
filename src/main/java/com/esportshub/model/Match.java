package com.esportshub.model;

// Represents a single match between two teams in a tournament round
public class Match {
    public int matchId, round;
    public String player1, player2, winner;

    public Match(int matchId, int round, String player1, String player2) {
        this.matchId = matchId;
        this.round = round;
        this.player1 = player1;
        this.player2 = player2;
    }

    // Checks if a winner has been selected for this match
    public boolean isCompleted() {
        return winner != null;
    }
}