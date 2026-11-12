package com.esportshub.auth;

import com.esportshub.core.MainDashboardView;
import com.esportshub.db.DatabaseManager;
import com.esportshub.db.HubDAO;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginView() {
        setTitle("Esports Hub - Staff Login");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(24, 28, 36));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        JLabel titleLabel = new JLabel("ESPORTS HUB LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 212, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.LIGHT_GRAY);
        mainPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.putClientProperty("JTextField.placeholderText", "Enter username");
        mainPanel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.LIGHT_GRAY);
        mainPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.putClientProperty("JTextField.placeholderText", "Enter password");
        mainPanel.add(passwordField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(18, 5, 5, 5);
        loginButton = new JButton("Sign In");
        loginButton.setBackground(new Color(0, 150, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(0, 40));
        mainPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());

        add(mainPanel);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String role = HubDAO.authenticateUser(username, password);
        if (role != null) {
            SessionContext.login(username, role);
            dispose();
            SwingUtilities.invokeLater(() -> new MainDashboardView().setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                new LoginView().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}