package com.esportshub.core;

import javax.swing.*;
import java.awt.*;

public class AppTheme {
    public static final Color BG = new Color(20, 24, 34);
    public static final Color CARD = new Color(32, 38, 52);
    public static final Color PURPLE = new Color(138, 43, 226);
    public static final Color GREEN = new Color(40, 180, 99);
    public static final Color RED = new Color(220, 53, 69);

    public static JButton button(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        return b;
    }
}