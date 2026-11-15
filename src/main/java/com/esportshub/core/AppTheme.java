package com.esportshub.core;

import javax.swing.*;
import java.awt.*;

// Centralized color palette and UI helper methods for consistent styling
public class AppTheme {
    public static final Color BG_DARK = new Color(18, 22, 30), BG_CARD = new Color(28, 34, 46);
    public static final Color CYAN = new Color(0, 212, 255), PURPLE = new Color(138, 43, 226);
    public static final Color GREEN = new Color(40, 180, 99), RED = new Color(220, 53, 69), GOLD = new Color(255, 193, 7);

    // Creates a styled button with custom background color
    public static JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(Color.WHITE); b.setFocusPainted(false);
        return b;
    }

    // Creates a colored JLabel
    public static JLabel lbl(String text, Color fg) {
        JLabel l = new JLabel(text);
        l.setForeground(fg);
        return l;
    }
}