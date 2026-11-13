package com.esportshub.ui.tournament;

import com.esportshub.core.AppTheme;
import com.esportshub.engine.TournamentEngine;
import com.esportshub.model.Match;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class TournamentPanel extends JPanel {
    private TournamentEngine engine;
    private JPanel bracketPanel = new JPanel(new GridLayout(1, 3, 18, 18));
    private JLabel champLabel = new JLabel("Champion: TBD");

    public TournamentPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(AppTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        top.setBackground(AppTheme.BG_CARD);

        JButton startBtn = new JButton("Start New 8-Team Tournament");
        AppTheme.styleButton(startBtn, AppTheme.ACCENT_PURPLE);

        champLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        champLabel.setForeground(AppTheme.GOLD);

        top.add(startBtn);
        top.add(champLabel);

        bracketPanel.setBackground(AppTheme.BG_DARK);

        startBtn.addActionListener(e -> {
            List<String> teams = Arrays.asList("Falcons", "NAVI", "Liquid", "Cloud9", "FaZe", "Fnatic", "G2", "Vitality");
            engine = new TournamentEngine(teams);
            drawBracket();
        });

        add(top, BorderLayout.NORTH);
        add(bracketPanel, BorderLayout.CENTER);
    }

    private void drawBracket() {
        bracketPanel.removeAll();
        bracketPanel.add(buildRoundColumn("QUARTERFINALS", engine.round1));
        bracketPanel.add(buildRoundColumn("SEMIFINALS", engine.round2));
        bracketPanel.add(buildRoundColumn("GRAND FINAL", engine.round3));
        champLabel.setText("Champion: " + engine.champion);
        bracketPanel.revalidate();
        bracketPanel.repaint();
    }

    private JPanel buildRoundColumn(String title, List<Match> matches) {
        JPanel col = new JPanel(new GridLayout(5, 1, 10, 10));
        col.setBackground(AppTheme.BG_CARD);
        col.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel(title, SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setForeground(AppTheme.ACCENT_CYAN);
        col.add(header);

        for (Match m : matches) {
            JPanel box = new JPanel(new GridLayout(2, 1, 4, 4));
            box.setBackground(AppTheme.BG_DARK);
            JButton b1 = new JButton(m.player1);
            JButton b2 = new JButton(m.player2);

            AppTheme.styleButton(b1, m.player1.equals(m.winner) ? AppTheme.GREEN : AppTheme.BG_SIDEBAR);
            AppTheme.styleButton(b2, m.player2.equals(m.winner) ? AppTheme.GREEN : AppTheme.BG_SIDEBAR);

            b1.setEnabled(!m.isCompleted());
            b2.setEnabled(!m.isCompleted());
            b1.addActionListener(e -> { engine.setWinner(m, m.player1); drawBracket(); });
            b2.addActionListener(e -> { engine.setWinner(m, m.player2); drawBracket(); });

            box.add(b1);
            box.add(b2);
            col.add(box);
        }
        return col;
    }
}