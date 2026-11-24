package com.esportshub;

import com.esportshub.db.Database;
import com.esportshub.ui.LoginFrame;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Database.init();
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(new FlatDarkLaf()); } catch (Exception ignored) {}
            new LoginFrame().setVisible(true);
        });
    }
}
