package com.esportshub.model;

public class Match {
    private int matchId;
    private int round; // 1 = Quarterfinals, 2 = Semifinals, 3 = Final
    private String player1;
    private String player2;
    private String winner;

    public Match(int matchId, int round, String player1, String player2) {
        this.matchId = matchId;
        this.round = round;
        this.player1 = player1;
        this.player2 = player2;
        this.winner = null;
    }

    public int getMatchId() { return matchId; }
    public int getRound() { return round; }
    public String getPlayer1() { return player1; }
    public void setPlayer1(String player1) { this.player1 = player1; }
    public String getPlayer2() { return player2; }
    public void setPlayer2(String player2) { this.player2 = player2; }
    public String getWinner() { return winner; }

    public void setWinner(String winner) {
        if (winner != null && (winner.equals(player1) || winner.equals(player2))) {
            this.winner = winner;
        }
    }

    public boolean isCompleted() {
        return winner != null;
    }

    @Override
    public String toString() {
        return String.format("Match %d (Round %d): %s vs %s -> Winner: %s",
                matchId, round, player1, player2, (winner != null ? winner : "TBD"));
    }
}