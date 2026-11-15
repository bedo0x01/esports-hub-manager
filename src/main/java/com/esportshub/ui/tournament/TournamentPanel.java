package com.esportshub.ui.tournament;

import com.esportshub.core.AppTheme;
import com.esportshub.engine.TournamentEngine;
import com.esportshub.model.Match;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

// Interactive 3-round tournament bracket UI
public class TournamentPanel extends JPanel {
    private TournamentEngine engine;
    private JPanel bracket = new JPanel(new GridLayout(1, 3, 15, 15));
    private JLabel champ = AppTheme.lbl("Champion: TBD", AppTheme.GOLD);

    public TournamentPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(AppTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        bracket.setBackground(AppTheme.BG_DARK);

        // Starts a new tournament with 8 shuffled teams
        JButton start = AppTheme.btn("Start 8-Team Tournament", AppTheme.PURPLE);
        start.addActionListener(e -> {
            engine = new TournamentEngine(Arrays.asList("Falcons", "NAVI", "Liquid", "Cloud9", "FaZe", "Fnatic", "G2", "Vitality"));
            draw();
        });

        JPanel top = new JPanel(); top.setBackground(AppTheme.BG_CARD);
        top.add(start); top.add(champ);
        add(top, BorderLayout.NORTH); add(bracket, BorderLayout.CENTER);
    }

    // Re-renders Quarterfinals, Semifinals, and Grand Final columns
    private void draw() {
        bracket.removeAll();
        bracket.add(col("Quarterfinals", engine.round1));
        bracket.add(col("Semifinals", engine.round2));
        bracket.add(col("Final", engine.round3));
        champ.setText("Champion: " + engine.champion);
        bracket.revalidate(); bracket.repaint();
    }

    // Builds a single round column with clickable team buttons to advance winners
    private JPanel col(String title, List<Match> matches) {
        JPanel c = new JPanel(new GridLayout(5, 1, 10, 10));
        c.setBackground(AppTheme.BG_CARD);
        c.add(new JLabel(title, SwingConstants.CENTER));
        for (Match m : matches) {
            JPanel box = new JPanel(new GridLayout(2, 1));
            JButton b1 = AppTheme.btn(m.player1, m.player1.equals(m.winner) ? AppTheme.GREEN : AppTheme.BG_DARK);
            JButton b2 = AppTheme.btn(m.player2, m.player2.equals(m.winner) ? AppTheme.GREEN : AppTheme.BG_DARK);
            b1.setEnabled(!m.isCompleted()); b2.setEnabled(!m.isCompleted());
            b1.addActionListener(e -> { engine.setWinner(m, m.player1); draw(); });
            b2.addActionListener(e -> { engine.setWinner(m, m.player2); draw(); });
            box.add(b1); box.add(b2); c.add(box);
        }
        return c;
    }
}