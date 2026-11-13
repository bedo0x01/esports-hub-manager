package com.esportshub.auth;

import com.esportshub.core.AppTheme;
import com.esportshub.core.MainDashboardView;
import com.esportshub.db.HubDAO;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    public LoginView() {
        setTitle("Esports Hub - Login");
        setSize(380, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 12));
        panel.setBackground(AppTheme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("ESPORTS HUB");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(AppTheme.ACCENT_CYAN);

        JLabel uLbl = new JLabel("Username:");
        uLbl.setForeground(AppTheme.TEXT_WHITE);
        JLabel pLbl = new JLabel("Password:");
        pLbl.setForeground(AppTheme.TEXT_WHITE);

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Sign In");
        AppTheme.styleButton(loginBtn, AppTheme.ACCENT_PURPLE);

        panel.add(title);
        panel.add(new JLabel(""));
        panel.add(uLbl);
        panel.add(userField);
        panel.add(pLbl);
        panel.add(passField);
        panel.add(new JLabel(""));
        panel.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String u = userField.getText().trim();
            String p = new String(passField.getPassword()).trim();
            String role = HubDAO.login(u, p);
            if (role != null) {
                SessionContext.username = u;
                SessionContext.role = role;
                dispose();
                new MainDashboardView().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Wrong username or password!");
            }
        });

        add(panel);
    }
}