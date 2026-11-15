package com.esportshub.engine;

import com.esportshub.model.Match;
import java.util.*;

// Handles 8-team random bracket draw and automatic round progression
public class TournamentEngine {
    public List<Match> round1 = new ArrayList<>(), round2 = new ArrayList<>(), round3 = new ArrayList<>();
    public String champion = "TBD";

    // Shuffles 8 teams randomly and generates Quarterfinal matches
    public TournamentEngine(List<String> teams) {
        Collections.shuffle(teams);
        for (int i = 0; i < 8; i += 2) round1.add(new Match((i / 2) + 1, 1, teams.get(i), teams.get(i + 1)));
    }

    // Sets match winner and advances winners to Semifinals, Final, or Champion
    public void setWinner(Match m, String winner) {
        m.winner = winner;
        if (m.round == 1 && done(round1) && round2.isEmpty()) {
            round2.add(new Match(5, 2, round1.get(0).winner, round1.get(1).winner));
            round2.add(new Match(6, 2, round1.get(2).winner, round1.get(3).winner));
        } else if (m.round == 2 && done(round2) && round3.isEmpty()) {
            round3.add(new Match(7, 3, round2.get(0).winner, round2.get(1).winner));
        } else if (m.round == 3 && done(round3)) {
            champion = round3.get(0).winner;
        }
    }

    // Checks if all matches in a round are finished
    private boolean done(List<Match> list) {
        for (Match m : list) if (!m.isCompleted()) return false;
        return true;
    }
}