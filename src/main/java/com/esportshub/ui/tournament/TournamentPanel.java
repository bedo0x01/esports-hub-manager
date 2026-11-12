package com.esportshub.ui.tournament;

import com.esportshub.engine.TournamentEngine;
import com.esportshub.model.Match;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TournamentPanel extends JPanel {
    private final JTextField[] teamFields = new JTextField[8];
    private JPanel bracketContainer;
    private JLabel championLabel;
    private TournamentEngine tournamentEngine;

    public TournamentPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(18, 21, 28));

        // Top Header & Champion Banner
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel headerLabel = new JLabel("ESPORTS TOURNAMENT BRACKET (8 TEAMS)");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(new Color(0, 212, 255));

        championLabel = new JLabel("Champion: TBD");
        championLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        championLabel.setForeground(new Color(255, 193, 7));

        topPanel.add(headerLabel, BorderLayout.WEST);
        topPanel.add(championLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Left Panel: 8 Team Inputs & Controls
        add(createTeamSetupPanel(), BorderLayout.WEST);

        // Center Panel: Dynamic 3-Round Bracket Display
        bracketContainer = new JPanel(new GridLayout(1, 3, 20, 0));
        bracketContainer.setBackground(new Color(18, 21, 28));
        add(bracketContainer, BorderLayout.CENTER);
    }

    private JPanel createTeamSetupPanel() {
        JPanel setupPanel = new JPanel();
        setupPanel.setLayout(new BoxLayout(setupPanel, BoxLayout.Y_AXIS));
        setupPanel.setBackground(new Color(28, 33, 44));
        setupPanel.setPreferredSize(new Dimension(260, 0));
        setupPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 52, 68), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel title = new JLabel("Registered Teams");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        setupPanel.add(title);
        setupPanel.add(Box.createVerticalStrut(10));

        for (int i = 0; i < 8; i++) {
            JPanel row = new JPanel(new BorderLayout(5, 0));
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(240, 32));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel numLabel = new JLabel((i + 1) + ".");
            numLabel.setForeground(Color.LIGHT_GRAY);
            numLabel.setPreferredSize(new Dimension(20, 25));

            teamFields[i] = new JTextField();
            teamFields[i].putClientProperty("JTextField.placeholderText", "Team " + (i + 1));

            row.add(numLabel, BorderLayout.WEST);
            row.add(teamFields[i], BorderLayout.CENTER);
            setupPanel.add(row);
            setupPanel.add(Box.createVerticalStrut(8));
        }

        setupPanel.add(Box.createVerticalStrut(10));

        JButton autoFillBtn = new JButton("Auto-Fill Sample Teams");
        autoFillBtn.setMaximumSize(new Dimension(240, 35));
        autoFillBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        autoFillBtn.setFocusPainted(false);
        autoFillBtn.addActionListener(e -> autoFillTeams());
        setupPanel.add(autoFillBtn);

        setupPanel.add(Box.createVerticalStrut(8));

        JButton startDrawBtn = new JButton("Shuffle & Start Bracket");
        startDrawBtn.setBackground(new Color(0, 150, 255));
        startDrawBtn.setForeground(Color.WHITE);
        startDrawBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        startDrawBtn.setMaximumSize(new Dimension(240, 40));
        startDrawBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        startDrawBtn.setFocusPainted(false);
        startDrawBtn.addActionListener(e -> startTournament());
        setupPanel.add(startDrawBtn);

        return setupPanel;
    }

    private void autoFillTeams() {
        List<String> sampleTeams = Arrays.asList(
                "Falcons", "Natus Vincere", "Team Liquid", "Cloud9",
                "FaZe Clan", "Fnatic", "G2 Esports", "Vitality"
        );
        for (int i = 0; i < 8; i++) {
            teamFields[i].setText(sampleTeams.get(i));
        }
    }

    private void startTournament() {
        List<String> teams = new ArrayList<>();
        for (JTextField field : teamFields) {
            String name = field.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter names for all 8 teams.", "Missing Teams", JOptionPane.WARNING_MESSAGE);
                return;
            }
            teams.add(name);
        }

        tournamentEngine = new TournamentEngine(teams);
        championLabel.setText("Champion: TBD");
        renderBracket();
    }

    private void renderBracket() {
        bracketContainer.removeAll();

        String[] roundTitles = {"Quarterfinals (Round 1)", "Semifinals (Round 2)", "Grand Final (Round 3)"};

        for (int round = 1; round <= 3; round++) {
            JPanel roundColumn = new JPanel();
            roundColumn.setLayout(new BoxLayout(roundColumn, BoxLayout.Y_AXIS));
            roundColumn.setBackground(new Color(24, 28, 36));
            roundColumn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(45, 52, 68), 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel roundHeader = new JLabel(roundTitles[round - 1]);
            roundHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
            roundHeader.setForeground(new Color(0, 212, 255));
            roundHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
            roundColumn.add(roundHeader);
            roundColumn.add(Box.createVerticalStrut(15));

            List<Match> matches = tournamentEngine.getRounds().get(round);
            if (matches != null) {
                for (Match match : matches) {
                    roundColumn.add(createMatchCard(round, match));
                    roundColumn.add(Box.createVerticalStrut(15));
                }
            } else {
                JLabel waitingLabel = new JLabel("Waiting for previous round...");
                waitingLabel.setForeground(Color.GRAY);
                waitingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                roundColumn.add(waitingLabel);
            }

            bracketContainer.add(roundColumn);
        }

        if (tournamentEngine.getChampion() != null) {
            championLabel.setText("Champion: " + tournamentEngine.getChampion());
        }

        bracketContainer.revalidate();
        bracketContainer.repaint();
    }

    private JPanel createMatchCard(int round, Match match) {
        JPanel card = new JPanel(new GridLayout(3, 1, 5, 5));
        card.setMaximumSize(new Dimension(280, 120));
        card.setBackground(new Color(34, 40, 54));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(match.isCompleted() ? new Color(40, 167, 69) : new Color(70, 80, 100), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel matchHeader = new JLabel("Match #" + match.getMatchId() + (match.isCompleted() ? " [Winner: " + match.getWinner() + "]" : ""));
        matchHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        matchHeader.setForeground(match.isCompleted() ? new Color(40, 200, 110) : Color.LIGHT_GRAY);

        JButton p1Btn = new JButton(match.getPlayer1());
        JButton p2Btn = new JButton(match.getPlayer2());

        stylePlayerButton(p1Btn, match, match.getPlayer1());
        stylePlayerButton(p2Btn, match, match.getPlayer2());

        p1Btn.addActionListener(e -> {
            tournamentEngine.setMatchWinner(round, match.getMatchId(), match.getPlayer1());
            renderBracket();
            checkChampionAlert();
        });

        p2Btn.addActionListener(e -> {
            tournamentEngine.setMatchWinner(round, match.getMatchId(), match.getPlayer2());
            renderBracket();
            checkChampionAlert();
        });

        card.add(matchHeader);
        card.add(p1Btn);
        card.add(p2Btn);
        return card;
    }

    private void stylePlayerButton(JButton btn, Match match, String playerName) {
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        if (match.isCompleted()) {
            btn.setEnabled(false);
            if (playerName.equals(match.getWinner())) {
                btn.setBackground(new Color(40, 167, 69));
                btn.setForeground(Color.WHITE);
            }
        }
    }

    private void checkChampionAlert() {
        if (tournamentEngine.getChampion() != null) {
            JOptionPane.showMessageDialog(this,
                    "Tournament Champion Crowned: " + tournamentEngine.getChampion() + "!",
                    "Tournament Completed", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}