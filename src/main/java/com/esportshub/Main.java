package com.esportshub;

import com.esportshub.auth.LoginView;
import com.esportshub.db.DatabaseManager;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Initialize SQLite tables and seed default data
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