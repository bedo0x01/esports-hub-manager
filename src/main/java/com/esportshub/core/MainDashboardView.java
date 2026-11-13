package com.esportshub.core;

import com.esportshub.auth.*;
import com.esportshub.reports.ReportsPanel;
import com.esportshub.ui.canteen.CanteenPanel;
import com.esportshub.ui.stations.StationGridPanel;
import com.esportshub.ui.tournament.TournamentPanel;
import javax.swing.*;
import java.awt.*;

public class MainDashboardView extends JFrame {
    public MainDashboardView() {
        setTitle("Esports Hub Manager - " + SessionContext.username + " (" + SessionContext.role + ")");
        setSize(1150, 720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppTheme.BG_DARK);

        CardLayout cards = new CardLayout();
        JPanel content = new JPanel(cards);
        content.setBackground(AppTheme.BG_DARK);
        content.add(new StationGridPanel(), "STATIONS");
        content.add(new CanteenPanel(), "CANTEEN");
        content.add(new TournamentPanel(), "TOURNAMENT");
        content.add(new ReportsPanel(), "REPORTS");
        if (SessionContext.isAdmin()) content.add(new StaffManagementPanel(), "STAFF");

        JPanel menu = new JPanel(new GridLayout(7, 1, 10, 12));
        menu.setBackground(AppTheme.BG_SIDEBAR);
        menu.setPreferredSize(new Dimension(200, 0));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 12, 20, 12));

        JLabel logo = new JLabel("ESPORTS HUB", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(AppTheme.ACCENT_CYAN);

        JButton b1 = new JButton("Stations");
        JButton b2 = new JButton("Canteen");
        JButton b3 = new JButton("Tournament");
        JButton b4 = new JButton("Reports");
        JButton b5 = new JButton("Staff");
        JButton b6 = new JButton("Logout");

        AppTheme.styleButton(b1, AppTheme.BG_CARD);
        AppTheme.styleButton(b2, AppTheme.BG_CARD);
        AppTheme.styleButton(b3, AppTheme.BG_CARD);
        AppTheme.styleButton(b4, AppTheme.BG_CARD);
        AppTheme.styleButton(b5, AppTheme.ACCENT_PURPLE);
        AppTheme.styleButton(b6, AppTheme.RED);

        b1.addActionListener(e -> cards.show(content, "STATIONS"));
        b2.addActionListener(e -> cards.show(content, "CANTEEN"));
        b3.addActionListener(e -> cards.show(content, "TOURNAMENT"));
        b4.addActionListener(e -> cards.show(content, "REPORTS"));
        b5.addActionListener(e -> cards.show(content, "STAFF"));
        b6.addActionListener(e -> { dispose(); new LoginView().setVisible(true); });

        menu.add(logo);
        menu.add(b1);
        menu.add(b2);
        menu.add(b3);
        menu.add(b4);
        if (SessionContext.isAdmin()) menu.add(b5);
        menu.add(b6);

        add(menu, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }
}