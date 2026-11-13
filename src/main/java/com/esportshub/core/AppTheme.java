package com.esportshub.core;

import javax.swing.*;
import java.awt.*;

public class AppTheme {
    public static final Color BG_DARK = new Color(18, 22, 30);
    public static final Color BG_CARD = new Color(28, 34, 46);
    public static final Color BG_SIDEBAR = new Color(22, 27, 38);

    public static final Color ACCENT_CYAN = new Color(0, 212, 255);
    public static final Color ACCENT_PURPLE = new Color(138, 43, 226);
    public static final Color GREEN = new Color(40, 180, 99);
    public static final Color RED = new Color(220, 53, 69);
    public static final Color GOLD = new Color(255, 193, 7);
    public static final Color TEXT_WHITE = new Color(240, 244, 248);
    public static final Color TEXT_GRAY = new Color(160, 170, 185);

    public static void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void styleTable(JTable table) {
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_WHITE);
        table.setRowHeight(28);
        table.setGridColor(new Color(45, 55, 72));
        table.getTableHeader().setBackground(BG_SIDEBAR);
        table.getTableHeader().setForeground(ACCENT_CYAN);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
}