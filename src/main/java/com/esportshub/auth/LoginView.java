package com.esportshub.auth;

import com.esportshub.core.*;
import com.esportshub.db.HubDAO;
import javax.swing.*;
import java.awt.*;

// Login screen authenticating users against SQLite database
public class LoginView extends JFrame {
    public LoginView() {
        setTitle("Esports Hub - Login");
        setSize(340, 210);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(3, 2, 10, 12));
        p.setBackground(AppTheme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField u = new JTextField();
        JPasswordField pass = new JPasswordField();
        JButton btn = AppTheme.btn("Sign In", AppTheme.PURPLE);

        p.add(AppTheme.lbl("Username:", Color.WHITE)); p.add(u);
        p.add(AppTheme.lbl("Password:", Color.WHITE)); p.add(pass);
        p.add(new JLabel()); p.add(btn);

        // Verifies credentials and opens MainDashboardView on success
        btn.addActionListener(e -> {
            String role = HubDAO.login(u.getText().trim(), new String(pass.getPassword()).trim());
            if (role != null) {
                SessionContext.username = u.getText().trim();
                SessionContext.role = role;
                dispose();
                new MainDashboardView().setVisible(true);
            } else JOptionPane.showMessageDialog(this, "Invalid login!");
        });

        add(p);
    }
}