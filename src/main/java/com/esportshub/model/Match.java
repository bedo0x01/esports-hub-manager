package com.esportshub.model;

public class Match {
    public int matchId;
    public int round;
    public String player1;
    public String player2;
    public String winner;

    public Match(int matchId, int round, String player1, String player2) {
        this.matchId = matchId;
        this.round = round;
        this.player1 = player1;
        this.player2 = player2;
    }

    public boolean isCompleted() {
        return winner != null;
    }
}