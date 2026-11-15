package com.esportshub.ui.stations;

import com.esportshub.core.AppTheme;
import com.esportshub.db.HubDAO;
import com.esportshub.engine.*;
import com.esportshub.model.Station;
import javax.swing.*;
import java.awt.*;

// Live dashboard grid displaying all stations with 1-second Swing Timer updates
public class StationGridPanel extends JPanel {
    public StationGridPanel() {
        setLayout(new GridLayout(0, 3, 15, 15));
        setBackground(AppTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (Station s : HubDAO.getAllStations()) {
            JPanel card = new JPanel(new GridLayout(4, 1, 5, 5));
            card.setBackground(AppTheme.BG_CARD);
            JLabel title = new JLabel(s.name + " (" + (int) s.hourlyRate + " EGP/hr)", SwingConstants.CENTER);
            title.setForeground(Color.WHITE);
            JLabel status = new JLabel("AVAILABLE", SwingConstants.CENTER), time = new JLabel("00:00:00 | 0.00 EGP", SwingConstants.CENTER);
            time.setForeground(AppTheme.GOLD);
            JButton btn = AppTheme.btn("Start", AppTheme.GREEN);

            // Toggles between starting a new session and checking out an active session
            btn.addActionListener(e -> {
                if (!StationTimerService.isBusy(s.id)) {
                    String p = JOptionPane.showInputDialog(this, "Player Name:", "Guest");
                    if (p != null && !p.isEmpty()) StationTimerService.startSession(s.id, p, s.hourlyRate);
                } else {
                    ActiveSession f = StationTimerService.endSession(s.id);
                    HubDAO.saveSale(s.name, f.customerName, f.getTimeCost(), f.canteenTotal, f.getTotal());
                    JOptionPane.showMessageDialog(this, "Total Bill: " + String.format("%.2f EGP", f.getTotal()));
                }
            });

            // Updates card colors, timer, and running bill every 1000ms safely on Swing EDT
            new Timer(1000, e -> {
                boolean busy = StationTimerService.isBusy(s.id);
                ActiveSession a = StationTimerService.getSession(s.id);
                status.setText(busy ? "BUSY - " + a.customerName : "AVAILABLE");
                status.setForeground(busy ? AppTheme.RED : AppTheme.GREEN);
                time.setText(busy ? a.getFormattedTime() + " | " + String.format("%.2f EGP", a.getTotal()) : "00:00:00 | 0.00 EGP");
                btn.setText(busy ? "End & Pay" : "Start");
                btn.setBackground(busy ? AppTheme.RED : AppTheme.GREEN);
                card.setBorder(BorderFactory.createLineBorder(busy ? AppTheme.RED : AppTheme.GREEN, 2));
            }).start();

            card.add(title); card.add(status); card.add(time); card.add(btn);
            add(card);
        }
    }
}