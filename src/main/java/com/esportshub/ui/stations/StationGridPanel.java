package com.esportshub.ui.stations;

import com.esportshub.core.AppTheme;
import com.esportshub.db.HubDAO;
import com.esportshub.engine.ActiveSession;
import com.esportshub.engine.StationTimerService;
import com.esportshub.model.Station;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StationGridPanel extends JPanel {
    public StationGridPanel() {
        setLayout(new GridLayout(0, 3, 18, 18));
        setBackground(AppTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        List<Station> stations = HubDAO.getAllStations();
        for (Station s : stations) {
            JPanel card = new JPanel(new GridLayout(4, 1, 6, 6));
            card.setBackground(AppTheme.BG_CARD);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppTheme.GREEN, 2),
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));

            JLabel nameLbl = new JLabel(s.name + "  [" + (int) s.hourlyRate + " EGP/hr]", SwingConstants.CENTER);
            nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            nameLbl.setForeground(AppTheme.TEXT_WHITE);

            JLabel statusLbl = new JLabel("AVAILABLE", SwingConstants.CENTER);
            statusLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            statusLbl.setForeground(AppTheme.GREEN);

            JLabel timeLbl = new JLabel("00:00:00  |  0.00 EGP", SwingConstants.CENTER);
            timeLbl.setFont(new Font("Consolas", Font.BOLD, 15));
            timeLbl.setForeground(AppTheme.GOLD);

            JButton btn = new JButton("Start Session");
            AppTheme.styleButton(btn, AppTheme.GREEN);

            btn.addActionListener(e -> {
                if (!StationTimerService.isBusy(s.id)) {
                    String player = JOptionPane.showInputDialog(this, "Player Name:", "Guest");
                    if (player != null && !player.trim().isEmpty()) {
                        StationTimerService.startSession(s.id, player.trim(), s.hourlyRate);
                    }
                } else {
                    ActiveSession finished = StationTimerService.endSession(s.id);
                    HubDAO.saveSale(s.name, finished.customerName, finished.getTimeCost(), finished.canteenTotal, finished.getTotal());
                    JOptionPane.showMessageDialog(this, "Total Bill: " + String.format("%.2f EGP", finished.getTotal()));
                }
            });

            new Timer(1000, e -> {
                if (StationTimerService.isBusy(s.id)) {
                    ActiveSession act = StationTimerService.getSession(s.id);
                    statusLbl.setText("BUSY - " + act.customerName);
                    statusLbl.setForeground(AppTheme.RED);
                    timeLbl.setText(act.getFormattedTime() + "  |  " + String.format("%.2f EGP", act.getTotal()));
                    btn.setText("End & Checkout");
                    btn.setBackground(AppTheme.RED);
                    card.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(AppTheme.RED, 2),
                            BorderFactory.createEmptyBorder(12, 12, 12, 12)
                    ));
                } else {
                    statusLbl.setText("AVAILABLE");
                    statusLbl.setForeground(AppTheme.GREEN);
                    timeLbl.setText("00:00:00  |  0.00 EGP");
                    btn.setText("Start Session");
                    btn.setBackground(AppTheme.GREEN);
                    card.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(AppTheme.GREEN, 2),
                            BorderFactory.createEmptyBorder(12, 12, 12, 12)
                    ));
                }
            }).start();

            card.add(nameLbl);
            card.add(statusLbl);
            card.add(timeLbl);
            card.add(btn);
            add(card);
        }
    }
}