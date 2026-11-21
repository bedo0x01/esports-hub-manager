package com.esportshub.auth;

import com.esportshub.core.*;
import com.esportshub.db.HubDAO;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    public LoginView() {
        setTitle("Esports Hub - Login");
        setSize(320, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = AppTheme.button("Login", AppTheme.PURPLE);

        add(new JLabel("Username:")); add(userField);
        add(new JLabel("Password:")); add(passField);
        add(new JLabel(""));          add(loginBtn);

        loginBtn.addActionListener(e -> {
            String role = HubDAO.login(userField.getText(), new String(passField.getPassword()));
            if (role != null) {
                SessionContext.user = userField.getText();
                SessionContext.role = role;
                dispose();
                new MainDashboardView().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Wrong username or password!");
            }
        });
    }
}