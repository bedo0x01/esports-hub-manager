package com.esportshub.reports;

import com.esportshub.auth.SessionContext;
import com.esportshub.core.AppTheme;
import com.esportshub.db.HubDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Displays sales history table, admin-only total revenue, and PDF export button
public class ReportsPanel extends JPanel {
    public ReportsPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(AppTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Station", "Customer", "Time", "Canteen", "Total", "Date"}, 0);
        JTable table = new JTable(model);
        JLabel rev = AppTheme.lbl("", AppTheme.GOLD);

        // Reloads sales records and hides total revenue from CASHIER users
        Runnable load = () -> {
            model.setRowCount(0);
            for (Object[] r : HubDAO.getAllSales()) model.addRow(r);
            rev.setText(SessionContext.isAdmin() ? "Total Revenue: " + HubDAO.getTotalRevenue() + " EGP" : "Revenue: Admin Only");
        };

        JButton ref = AppTheme.btn("Refresh", AppTheme.PURPLE), pdf = AppTheme.btn("Export Selected to PDF", AppTheme.GREEN);
        ref.addActionListener(e -> load.run());

        // Exports selected invoice row to a PDF file
        pdf.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r != -1) JOptionPane.showMessageDialog(this, "Saved: " + PdfInvoiceGenerator.createPdf(
                    model.getValueAt(r, 0).toString(), model.getValueAt(r, 1).toString(), model.getValueAt(r, 2).toString(),
                    model.getValueAt(r, 3).toString(), model.getValueAt(r, 4).toString(), model.getValueAt(r, 5).toString()));
        });

        JPanel top = new JPanel(new BorderLayout()); top.setBackground(AppTheme.BG_CARD);
        top.add(rev, BorderLayout.WEST); top.add(ref, BorderLayout.EAST);

        add(top, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER); add(pdf, BorderLayout.SOUTH);
        load.run();
    }
}