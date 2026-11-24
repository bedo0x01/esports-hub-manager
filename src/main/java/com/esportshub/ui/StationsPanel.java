package com.esportshub.ui;

import com.esportshub.db.Database;
import com.esportshub.engine.SessionEngine;
import javax.swing.*;
import java.awt.*;

public class StationsPanel extends JPanel {
    public StationsPanel() {
        setLayout(new GridLayout(0, 3, 15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (Object[] st : Database.query("SELECT * FROM stations")) {
            int id = (int) st[0];
            String name = (String) st[1];
            double rate = ((Number) st[2]).doubleValue();

            JPanel card = new JPanel(new GridLayout(4, 1, 5, 5));
            card.setBackground(new Color(32, 38, 52));

            JLabel title = new JLabel(name + " (" + rate + " EGP/hr)", SwingConstants.CENTER);
            JLabel status = new JLabel("AVAILABLE", SwingConstants.CENTER);
            JLabel bill = new JLabel("0 sec | 0.0 EGP", SwingConstants.CENTER);
            JButton btn = new JButton("Start");

            btn.addActionListener(e -> {
                if (!SessionEngine.isBusy(id)) {
                    String player = JOptionPane.showInputDialog(this, "Player Name:");
                    if (player != null && !player.isEmpty()) SessionEngine.start(id, player, rate);
                } else {
                    SessionEngine.Session s = SessionEngine.stop(id);
                    Database.execute("INSERT INTO sales (station,customer,time_cost,canteen_cost,total) VALUES (?,?,?,?,?)",
                            name, s.player, s.timeCost(), s.canteen, s.total());
                    JOptionPane.showMessageDialog(this, "Total Bill: " + s.total() + " EGP");
                }
            });

            new Timer(1000, e -> {
                boolean busy = SessionEngine.isBusy(id);
                SessionEngine.Session s = SessionEngine.active.get(id);
                status.setText(busy ? "BUSY: " + s.player : "AVAILABLE");
                status.setForeground(busy ? Color.RED : Color.GREEN);
                bill.setText(busy ? s.seconds() + " sec | " + s.total() + " EGP" : "0 sec | 0.0 EGP");
                btn.setText(busy ? "Stop & Pay" : "Start");
                btn.setBackground(busy ? new Color(220, 53, 69) : new Color(40, 180, 99));
            }).start();

            card.add(title); card.add(status); card.add(bill); card.add(btn);
            add(card);
        }
    }
}
