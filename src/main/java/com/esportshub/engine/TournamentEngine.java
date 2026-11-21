package com.esportshub.engine;

import com.esportshub.model.Match;
import java.util.*;

public class TournamentEngine {
    public List<Match> semi = new ArrayList<>();
    public Match finalMatch;
    public String champion = "TBD";

    public TournamentEngine(List<String> teams) {
        Collections.shuffle(teams);
        semi.add(new Match(teams.get(0), teams.get(1)));
        semi.add(new Match(teams.get(2), teams.get(3)));
    }

    public void pickWinner(Match m, String win) {
        m.winner = win;
        if (semi.get(0).winner != null && semi.get(1).winner != null && finalMatch == null) {
            finalMatch = new Match(semi.get(0).winner, semi.get(1).winner);
        } else if (m == finalMatch) {
            champion = win;
        }
    }
}