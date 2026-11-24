package com.esportshub.ui;

import com.esportshub.db.Database;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Esports Hub - Login");
        setSize(320, 170);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JTextField uField = new JTextField();
        JPasswordField pField = new JPasswordField();
        JButton btn = new JButton("Login");

        add(new JLabel("Username:")); add(uField);
        add(new JLabel("Password:")); add(pField);
        add(new JLabel(""));          add(btn);

        btn.addActionListener(e -> {
            if (Database.login(uField.getText(), new String(pField.getPassword()))) {
                dispose();
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login!");
            }
        });
    }

    private void showDashboard() {
        JFrame dash = new JFrame("Esports Hub - " + Database.currentUser + " (" + Database.currentRole + ")");
        dash.setSize(950, 600);
        dash.setDefaultCloseOperation(EXIT_ON_CLOSE);
        dash.setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Stations", new StationsPanel());
        tabs.addTab("Canteen", new CanteenPanel());
        tabs.addTab("Tournament", new TournamentPanel());
        tabs.addTab("Reports & Admin", new ReportsPanel());

        dash.add(tabs);
        dash.setVisible(true);
    }
}
