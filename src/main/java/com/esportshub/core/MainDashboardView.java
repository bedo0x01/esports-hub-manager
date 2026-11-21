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
        setTitle("Esports Hub - " + SessionContext.user + " (" + SessionContext.role + ")");
        setSize(950, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        CardLayout cards = new CardLayout();
        JPanel mainArea = new JPanel(cards);
        mainArea.add(new StationGridPanel(), "stations");
        mainArea.add(new CanteenPanel(), "canteen");
        mainArea.add(new TournamentPanel(), "tournament");
        mainArea.add(new ReportsPanel(), "reports");
        if (SessionContext.isAdmin()) mainArea.add(new StaffManagementPanel(), "staff");

        JPanel sideMenu = new JPanel(new GridLayout(6, 1, 8, 8));
        sideMenu.setBackground(AppTheme.CARD);

        JButton b1 = AppTheme.button("Stations", AppTheme.BG);
        JButton b2 = AppTheme.button("Canteen", AppTheme.BG);
        JButton b3 = AppTheme.button("Tournament", AppTheme.BG);
        JButton b4 = AppTheme.button("Reports", AppTheme.BG);
        JButton b5 = AppTheme.button("Staff", AppTheme.PURPLE);
        JButton b6 = AppTheme.button("Logout", AppTheme.RED);

        b1.addActionListener(e -> cards.show(mainArea, "stations"));
        b2.addActionListener(e -> cards.show(mainArea, "canteen"));
        b3.addActionListener(e -> cards.show(mainArea, "tournament"));
        b4.addActionListener(e -> cards.show(mainArea, "reports"));
        b5.addActionListener(e -> cards.show(mainArea, "staff"));
        b6.addActionListener(e -> { dispose(); new LoginView().setVisible(true); });

        sideMenu.add(b1); sideMenu.add(b2); sideMenu.add(b3); sideMenu.add(b4);
        if (SessionContext.isAdmin()) sideMenu.add(b5);
        sideMenu.add(b6);

        add(sideMenu, BorderLayout.WEST);
        add(mainArea, BorderLayout.CENTER);
    }
}