package com.esportshub;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                System.out.println("Esports Hub Manager initialized successfully!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}