package com.esportshub.ui.stations;

import com.esportshub.db.HubDAO;
import com.esportshub.engine.ActiveSession;
import com.esportshub.engine.StationTimerService;
import com.esportshub.model.Station;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationGridPanel extends JPanel {
    private final StationTimerService timerService = StationTimerService.getInstance();
    private final Map<Integer, StationCard> cardMap = new HashMap<>();
    private Timer uiRefreshTimer;

    public StationGridPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(18, 21, 28));

        // Top Header
        JLabel headerLabel = new JLabel("LIVE GAMING STATIONS");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(new Color(0, 212, 255));
        add(headerLabel, BorderLayout.NORTH);

        // Stations Grid (4 columns, auto rows)
        JPanel gridContainer = new JPanel(new GridLayout(0, 4, 15, 15));
        gridContainer.setBackground(new Color(18, 21, 28));

        List<Station> stations = HubDAO.getAllStations();
        for (Station station : stations) {
            StationCard card = new StationCard(station);
            cardMap.put(station.getId(), card);
            gridContainer.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(gridContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(18, 21, 28));
        add(scrollPane, BorderLayout.CENTER);

        // Start UI ticker every 1 second to update live timers and prices
        startLiveUiUpdater();
    }

    private void startLiveUiUpdater() {
        uiRefreshTimer = new Timer(1000, e -> {
            for (StationCard card : cardMap.values()) {
                card.refreshDisplay();
            }
        });
        uiRefreshTimer.start();
    }

    // Inner class representing a single station card
    private class StationCard extends JPanel {
        private final Station station;
        private final JLabel statusLabel;
        private final JLabel timerLabel;
        private final JLabel costLabel;
        private final JButton actionButton;

        public StationCard(Station station) {
            this.station = station;
            setLayout(new BorderLayout(8, 8));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(45, 52, 68), 2),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            setBackground(new Color(28, 33, 44));

            // Top: Station Name & Rate
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);
            JLabel nameLabel = new JLabel(station.getName());
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            nameLabel.setForeground(Color.WHITE);

            JLabel rateLabel = new JLabel(String.format("%.0f EGP/hr", station.getHourlyRate()));
            rateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            rateLabel.setForeground(Color.LIGHT_GRAY);

            topPanel.add(nameLabel, BorderLayout.WEST);
            topPanel.add(rateLabel, BorderLayout.EAST);

            // Center: Status, Timer & Live Cost
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
            centerPanel.setOpaque(false);

            statusLabel = new JLabel("AVAILABLE");
            statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            timerLabel = new JLabel("00:00:00");
            timerLabel.setFont(new Font("Consolas", Font.BOLD, 24));
            timerLabel.setForeground(Color.WHITE);
            timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            costLabel = new JLabel("0.00 EGP");
            costLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            costLabel.setForeground(new Color(255, 193, 7));
            costLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            centerPanel.add(Box.createVerticalStrut(5));
            centerPanel.add(statusLabel);
            centerPanel.add(Box.createVerticalStrut(8));
            centerPanel.add(timerLabel);
            centerPanel.add(Box.createVerticalStrut(5));
            centerPanel.add(costLabel);

            // Bottom: Start / Stop Button
            actionButton = new JButton("Start Session");
            actionButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            actionButton.setFocusPainted(false);
            actionButton.addActionListener(e -> handleAction());

            add(topPanel, BorderLayout.NORTH);
            add(centerPanel, BorderLayout.CENTER);
            add(actionButton, BorderLayout.SOUTH);

            refreshDisplay();
        }

        public void refreshDisplay() {
            boolean busy = timerService.isStationBusy(station.getId());
            if (busy) {
                ActiveSession session = timerService.getSession(station.getId());
                statusLabel.setText("BUSY (" + session.getCustomerName() + ")");
                statusLabel.setForeground(new Color(255, 82, 82));
                timerLabel.setText(session.getFormattedTime());
                costLabel.setText(String.format("%.2f EGP", session.getTotalAmount()));
                actionButton.setText("End & Checkout");
                actionButton.setBackground(new Color(220, 53, 69));
                actionButton.setForeground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 53, 69), 2),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            } else {
                statusLabel.setText("AVAILABLE");
                statusLabel.setForeground(new Color(40, 200, 110));
                timerLabel.setText("00:00:00");
                costLabel.setText("0.00 EGP");
                actionButton.setText("Start Session");
                actionButton.setBackground(new Color(40, 167, 69));
                actionButton.setForeground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(40, 167, 69), 2),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
        }

        private void handleAction() {
            if (!timerService.isStationBusy(station.getId())) {
                // Start new session
                String customer = JOptionPane.showInputDialog(this, "Enter Player Name:", "Guest");
                if (customer != null && !customer.trim().isEmpty()) {
                    timerService.startSession(station.getId(), customer.trim(), station.getHourlyRate());
                    HubDAO.updateStationStatus(station.getId(), "BUSY");
                    refreshDisplay();
                }
            } else {
                // End session & save receipt
                int confirm = JOptionPane.showConfirmDialog(this,
                        "End session for " + station.getName() + " and checkout?",
                        "Confirm Checkout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    ActiveSession finished = timerService.endSession(station.getId());
                    if (finished != null) {
                        HubDAO.updateStationStatus(station.getId(), "AVAILABLE");
                        HubDAO.saveSaleRecord(
                                station.getName(),
                                finished.getCustomerName(),
                                finished.getTimeCost(),
                                finished.getCanteenTotal(),
                                finished.getTotalAmount()
                        );
                        JOptionPane.showMessageDialog(this,
                                String.format("Receipt Summary (%s):\nPlayer: %s\nPlay Time: %s\nTime Cost: %.2f EGP\nCanteen: %.2f EGP\nTotal Due: %.2f EGP",
                                        station.getName(),
                                        finished.getCustomerName(),
                                        finished.getFormattedTime(),
                                        finished.getTimeCost(),
                                        finished.getCanteenTotal(),
                                        finished.getTotalAmount()),
                                "Session Completed", JOptionPane.INFORMATION_MESSAGE);
                        refreshDisplay();
                    }
                }
            }
        }
    }
}