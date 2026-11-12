package com.esportshub.engine;

import com.esportshub.model.Match;
import java.util.*;

public class TournamentEngine {
    private List<String> initialTeams;
    private Map<Integer, List<Match>> rounds = new LinkedHashMap<>();
    private String champion = null;

    public TournamentEngine(List<String> teams) {
        if (teams == null || (teams.size() != 8 && teams.size() != 4)) {
            throw new IllegalArgumentException("Tournament requires exactly 4 or 8 teams!");
        }
        this.initialTeams = new ArrayList<>(teams);
        generateFirstRound();
    }

    // Shuffle teams randomly and create first-round matchups
    private void generateFirstRound() {
        Collections.shuffle(initialTeams);
        List<Match> round1 = new ArrayList<>();
        int matchCounter = 1;

        for (int i = 0; i < initialTeams.size(); i += 2) {
            round1.add(new Match(matchCounter++, 1, initialTeams.get(i), initialTeams.get(i + 1)));
        }
        rounds.put(1, round1);
    }

    // Assign match winner and check if current round is complete
    public boolean setMatchWinner(int round, int matchId, String winner) {
        List<Match> currentRoundMatches = rounds.get(round);
        if (currentRoundMatches == null) {
            return false;
        }

        Match targetMatch = null;
        for (Match m : currentRoundMatches) {
            if (m.getMatchId() == matchId) {
                targetMatch = m;
                break;
            }
        }

        if (targetMatch == null) {
            return false;
        }

        targetMatch.setWinner(winner);

        // Check if all matches in the current round are finished
        boolean roundFinished = currentRoundMatches.stream().allMatch(Match::isCompleted);
        if (roundFinished) {
            advanceToNextRound(round);
        }
        return true;
    }

    // Advance winners to next stage (Semifinals / Final)
    private void advanceToNextRound(int completedRound) {
        List<Match> currentMatches = rounds.get(completedRound);
        List<String> winners = new ArrayList<>();
        for (Match m : currentMatches) {
            winners.add(m.getWinner());
        }

        // If only one winner remains, crown the champion
        if (winners.size() == 1) {
            this.champion = winners.get(0);
            return;
        }

        int nextRound = completedRound + 1;
        List<Match> nextRoundMatches = new ArrayList<>();
        int matchCounter = currentMatches.get(currentMatches.size() - 1).getMatchId() + 1;

        for (int i = 0; i < winners.size(); i += 2) {
            nextRoundMatches.add(new Match(matchCounter++, nextRound, winners.get(i), winners.get(i + 1)));
        }
        rounds.put(nextRound, nextRoundMatches);
    }

    public Map<Integer, List<Match>> getRounds() {
        return rounds;
    }

    public String getChampion() {
        return champion;
    }
}