package com.esportshub.core;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;

public class MainDashboardView extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainDashboardView() {
        setTitle("Esports Hub & Gaming Center Manager");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Top Header Bar
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 28, 36));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("ESPORTS HUB MANAGER");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 212, 255));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> handleLogout());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Left Navigation Sidebar
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(30, 34, 44));
        sidebarPanel.setPreferredSize(new Dimension(220, getHeight()));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton stationsBtn = createNavButton("Gaming Stations", "STATIONS");
        JButton canteenBtn = createNavButton("Canteen & POS", "CANTEEN");
        JButton tournamentBtn = createNavButton("Tournaments", "TOURNAMENT");
        JButton reportsBtn = createNavButton("Reports & Invoices", "REPORTS");

        sidebarPanel.add(stationsBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(canteenBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(tournamentBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(reportsBtn);

        add(sidebarPanel, BorderLayout.WEST);

        // 3. Center Content Area (CardLayout for switching screens)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Placeholder panels until team members finish their UIs
        contentPanel.add(createPlaceholderPanel("Eman's Station Live Grid goes here"), "STATIONS");
        contentPanel.add(createPlaceholderPanel("Shahd's Canteen POS goes here"), "CANTEEN");
        contentPanel.add(createPlaceholderPanel("Asmaa H2's Tournament Bracket goes here"), "TOURNAMENT");
        contentPanel.add(createPlaceholderPanel("Asmaa New's Invoices & Reports go here"), "REPORTS");

        add(contentPanel, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.addActionListener(e -> cardLayout.show(contentPanel, cardName));
        return btn;
    }

    private JPanel createPlaceholderPanel(String message) {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel(message);
        label.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        label.setForeground(Color.GRAY);
        panel.add(label);
        return panel;
    }

    // Method to plug in real panels once team members finish them
    public void registerModulePanel(JPanel panel, String cardName) {
        contentPanel.add(panel, cardName);
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            // Later: new LoginView().setVisible(true);
        }
    }

    // Quick test runner
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                new MainDashboardView().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}