package com.esportshub.core;

import com.esportshub.auth.LoginView;
import com.esportshub.auth.SessionContext;
import com.esportshub.auth.StaffManagementPanel;
import com.esportshub.reports.ReportsPanel;
import com.esportshub.ui.canteen.CanteenPanel;
import com.esportshub.ui.stations.StationGridPanel;
import com.esportshub.ui.tournament.TournamentPanel;

import javax.swing.*;
import java.awt.*;

public class MainDashboardView extends JFrame {
    private final JPanel contentPanel;
    private final CardLayout cardLayout;

    public MainDashboardView() {
        setTitle("Esports Hub & Gaming Center Manager");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(24, 28, 36));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("ESPORTS HUB MANAGER");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 212, 255));

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightHeader.setOpaque(false);

        String userText = SessionContext.getCurrentUsername() != null
                ? "Logged in: " + SessionContext.getCurrentUsername() + " (" + SessionContext.getCurrentRole() + ")"
                : "Guest Session";
        JLabel userBadge = new JLabel(userText);
        userBadge.setForeground(Color.LIGHT_GRAY);
        userBadge.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> handleLogout());

        rightHeader.add(userBadge);
        rightHeader.add(logoutBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(30, 34, 44));
        sidebarPanel.setPreferredSize(new Dimension(220, getHeight()));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        sidebarPanel.add(createNavButton("Gaming Stations", "STATIONS"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(createNavButton("Canteen & POS", "CANTEEN"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(createNavButton("Tournaments", "TOURNAMENT"));
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(createNavButton("Reports & Invoices", "REPORTS"));

        if (SessionContext.isAdmin()) {
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            sidebarPanel.add(createNavButton("Staff & Users", "STAFF"));
        }

        add(sidebarPanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new StationGridPanel(), "STATIONS");
        contentPanel.add(new CanteenPanel(), "CANTEEN");
        contentPanel.add(new TournamentPanel(), "TOURNAMENT");
        contentPanel.add(new ReportsPanel(), "REPORTS");

        if (SessionContext.isAdmin()) {
            contentPanel.add(new StaffManagementPanel(), "STAFF");
        }

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

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            SessionContext.logout();
            dispose();
            new LoginView().setVisible(true);
        }
    }
}