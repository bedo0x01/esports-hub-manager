package com.esportshub.engine;

import com.esportshub.model.Match;
import java.util.*;

public class TournamentEngine {
    public List<Match> round1 = new ArrayList<>();
    public List<Match> round2 = new ArrayList<>();
    public List<Match> round3 = new ArrayList<>();
    public String champion = "TBD";

    public TournamentEngine(List<String> teams) {
        Collections.shuffle(teams);
        for (int i = 0; i < 8; i += 2) {
            round1.add(new Match((i / 2) + 1, 1, teams.get(i), teams.get(i + 1)));
        }
    }

    public void setWinner(Match m, String winner) {
        m.winner = winner;

        if (m.round == 1 && allDone(round1) && round2.isEmpty()) {
            round2.add(new Match(5, 2, round1.get(0).winner, round1.get(1).winner));
            round2.add(new Match(6, 2, round1.get(2).winner, round1.get(3).winner));
        } else if (m.round == 2 && allDone(round2) && round3.isEmpty()) {
            round3.add(new Match(7, 3, round2.get(0).winner, round2.get(1).winner));
        } else if (m.round == 3 && allDone(round3)) {
            champion = round3.get(0).winner;
        }
    }

    private boolean allDone(List<Match> matches) {
        for (Match m : matches) {
            if (!m.isCompleted()) return false;
        }
        return true;
    }
}