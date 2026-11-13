package com.esportshub;

import com.esportshub.auth.LoginView;
import com.esportshub.db.DatabaseManager;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(new FlatDarkLaf()); } catch (Exception ignored) {}
            new LoginView().setVisible(true);
        });
    }
}