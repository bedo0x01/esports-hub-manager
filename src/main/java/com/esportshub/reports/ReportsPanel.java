package com.esportshub.reports;

import com.esportshub.auth.SessionContext;
import com.esportshub.core.AppTheme;
import com.esportshub.db.HubDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportsPanel extends JPanel {
    private DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Station", "Customer", "Time", "Canteen", "Total", "Date"}, 0);
    private JTable table = new JTable(model);
    private JLabel revLabel = new JLabel();

    public ReportsPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(AppTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(AppTheme.BG_CARD);
        top.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        revLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        revLabel.setForeground(AppTheme.GOLD);

        JButton refreshBtn = new JButton("Refresh");
        AppTheme.styleButton(refreshBtn, AppTheme.ACCENT_PURPLE);

        top.add(revLabel, BorderLayout.WEST);
        top.add(refreshBtn, BorderLayout.EAST);

        AppTheme.styleTable(table);

        JButton pdfBtn = new JButton("Export Selected Invoice to PDF");
        AppTheme.styleButton(pdfBtn, AppTheme.GREEN);

        refreshBtn.addActionListener(e -> load());
        pdfBtn.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) return;
            String path = PdfInvoiceGenerator.createPdf(
                    model.getValueAt(r, 0).toString(), model.getValueAt(r, 1).toString(),
                    model.getValueAt(r, 2).toString(), model.getValueAt(r, 3).toString(),
                    model.getValueAt(r, 4).toString(), model.getValueAt(r, 5).toString());
            JOptionPane.showMessageDialog(this, "Saved: " + path);
        });

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(pdfBtn, BorderLayout.SOUTH);
        load();
    }

    private void load() {
        model.setRowCount(0);
        for (Object[] row : HubDAO.getAllSales()) model.addRow(row);
        revLabel.setText(SessionContext.isAdmin() ? "Total Revenue: " + String.format("%.2f EGP", HubDAO.getTotalRevenue()) : "Revenue: Admin Only");
    }
}