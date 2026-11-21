package com.esportshub.ui.stations;

import com.esportshub.core.AppTheme;
import com.esportshub.db.HubDAO;
import com.esportshub.engine.*;
import com.esportshub.model.Station;
import javax.swing.*;
import java.awt.*;

public class StationGridPanel extends JPanel {
    public StationGridPanel() {
        setLayout(new GridLayout(0, 3, 12, 12));

        for (Station s : HubDAO.getStations()) {
            JPanel card = new JPanel(new GridLayout(4, 1));
            card.setBackground(AppTheme.CARD);

            JLabel nameLbl = new JLabel(s.name + " (" + s.rate + " EGP/hr)", SwingConstants.CENTER);
            JLabel statusLbl = new JLabel("AVAILABLE", SwingConstants.CENTER);
            JLabel billLbl = new JLabel("0 sec | 0.0 EGP", SwingConstants.CENTER);
            JButton btn = AppTheme.button("Start", AppTheme.GREEN);

            btn.addActionListener(e -> {
                if (!StationTimerService.isBusy(s.id)) {
                    String name = JOptionPane.showInputDialog(this, "Player Name:");
                    if (name != null && !name.isEmpty()) StationTimerService.start(s.id, name, s.rate);
                } else {
                    ActiveSession session = StationTimerService.stop(s.id);
                    HubDAO.addSale(s.name, session.player, session.getTimeCost(), session.canteenCost);
                    JOptionPane.showMessageDialog(this, "Total Bill: " + session.getTotal() + " EGP");
                }
            });

            new Timer(1000, e -> {
                boolean busy = StationTimerService.isBusy(s.id);
                ActiveSession a = StationTimerService.active.get(s.id);
                statusLbl.setText(busy ? "BUSY: " + a.player : "AVAILABLE");
                statusLbl.setForeground(busy ? AppTheme.RED : AppTheme.GREEN);
                billLbl.setText(busy ? a.getSeconds() + " sec | " + a.getTotal() + " EGP" : "0 sec | 0.0 EGP");
                btn.setText(busy ? "Stop & Pay" : "Start");
                btn.setBackground(busy ? AppTheme.RED : AppTheme.GREEN);
            }).start();

            card.add(nameLbl); card.add(statusLbl); card.add(billLbl); card.add(btn);
            add(card);
        }
    }
}