package com.esportshub;

import com.esportshub.auth.LoginView;
import com.esportshub.db.DatabaseManager;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

// Application entry point: initializes database, applies FlatDarkLaf theme, and opens LoginView
public class Main {
    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(new FlatDarkLaf()); } catch (Exception ignored) {}
            new LoginView().setVisible(true);
        });
    }
}