package com.esportshub.ui;

import com.esportshub.engine.TournamentEngine;
import com.esportshub.engine.TournamentEngine.Match;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class TournamentPanel extends JPanel {
    private TournamentEngine engine;
    private JPanel board = new JPanel(new GridLayout(1, 2, 15, 15));
    private JLabel champLbl = new JLabel("Champion: TBD");

    public TournamentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton startBtn = new JButton("Start 4-Team Tournament");
        startBtn.addActionListener(e -> {
            engine = new TournamentEngine(Arrays.asList("Falcons", "NAVI", "Liquid", "G2"));
            draw();
        });

        JPanel top = new JPanel();
        top.add(startBtn); top.add(champLbl);
        add(top, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
    }

    private void draw() {
        board.removeAll();
        JPanel semiCol = new JPanel(new GridLayout(2, 1, 10, 10));
        for (Match m : engine.semi) semiCol.add(matchBox(m));

        JPanel finalCol = new JPanel(new GridLayout(1, 1));
        if (engine.finalMatch != null) finalCol.add(matchBox(engine.finalMatch));

        board.add(semiCol); board.add(finalCol);
        champLbl.setText("Champion: " + engine.champion);
        board.revalidate(); board.repaint();
    }

    private JPanel matchBox(Match m) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        JButton b1 = new JButton(m.p1), b2 = new JButton(m.p2);
        if (m.p1.equals(m.winner)) b1.setBackground(new Color(40, 180, 99));
        if (m.p2.equals(m.winner)) b2.setBackground(new Color(40, 180, 99));
        b1.setEnabled(m.winner == null); b2.setEnabled(m.winner == null);
        b1.addActionListener(e -> { engine.setWinner(m, m.p1); draw(); });
        b2.addActionListener(e -> { engine.setWinner(m, m.p2); draw(); });
        p.add(b1); p.add(b2);
        return p;
    }
}
