package com.esportshub.core;

import com.esportshub.auth.*;
import com.esportshub.reports.ReportsPanel;
import com.esportshub.ui.canteen.CanteenPanel;
import com.esportshub.ui.stations.StationGridPanel;
import com.esportshub.ui.tournament.TournamentPanel;
import javax.swing.*;
import java.awt.*;

// Main application window using CardLayout for switching modules and RBAC visibility
public class MainDashboardView extends JFrame {
    public MainDashboardView() {
        setTitle("Esports Hub - " + SessionContext.username + " (" + SessionContext.role + ")");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        CardLayout cards = new CardLayout();
        JPanel content = new JPanel(cards);
        content.add(new StationGridPanel(), "1");
        content.add(new CanteenPanel(), "2");
        content.add(new TournamentPanel(), "3");
        content.add(new ReportsPanel(), "4");
                            // Staff management panel is only loaded for ADMIN role
        if (SessionContext.isAdmin()) content.add(new StaffManagementPanel(), "5");

        JPanel menu = new JPanel(new GridLayout(6, 1, 10, 10));
        menu.setBackground(AppTheme.BG_CARD);
        menu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton b1 = AppTheme.btn("Stations", AppTheme.BG_DARK), b2 = AppTheme.btn("Canteen", AppTheme.BG_DARK);
        JButton b3 = AppTheme.btn("Tournament", AppTheme.BG_DARK), b4 = AppTheme.btn("Reports", AppTheme.BG_DARK);
        JButton b5 = AppTheme.btn("Staff", AppTheme.PURPLE), b6 = AppTheme.btn("Logout", AppTheme.RED);

        b1.addActionListener(e -> cards.show(content, "1"));
        b2.addActionListener(e -> cards.show(content, "2"));
        b3.addActionListener(e -> cards.show(content, "3"));
        b4.addActionListener(e -> cards.show(content, "4"));
        b5.addActionListener(e -> cards.show(content, "5"));
        b6.addActionListener(e -> { dispose(); new LoginView().setVisible(true); });

        menu.add(b1); menu.add(b2); menu.add(b3); menu.add(b4);
        if (SessionContext.isAdmin()) menu.add(b5);
        menu.add(b6);

        add(menu, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }
}