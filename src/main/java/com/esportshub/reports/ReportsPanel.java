package com.esportshub.reports;

import com.esportshub.auth.SessionContext;
import com.esportshub.core.AppTheme;
import com.esportshub.db.HubDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportsPanel extends JPanel {
    public ReportsPanel() {
        setLayout(new BorderLayout(10, 10));
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Station", "Player", "Time", "Canteen", "Total"}, 0);
        JTable table = new JTable(model);
        JLabel revLabel = new JLabel();

        Runnable load = () -> {
            model.setRowCount(0);
            for (Object[] r : HubDAO.getSales()) model.addRow(r);
            revLabel.setText(SessionContext.isAdmin() ? "Total Revenue: " + HubDAO.getTotalRevenue() + " EGP" : "Revenue: Admin Only");
        };

        JButton refreshBtn = AppTheme.button("Refresh", AppTheme.PURPLE);
        JButton pdfBtn = AppTheme.button("Print PDF", AppTheme.GREEN);

        refreshBtn.addActionListener(e -> load.run());
        pdfBtn.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r != -1) {
                String path = PdfInvoiceGenerator.generate(
                        model.getValueAt(r, 0).toString(),
                        model.getValueAt(r, 1).toString(),
                        model.getValueAt(r, 2).toString(),
                        model.getValueAt(r, 5).toString());
                JOptionPane.showMessageDialog(this, "Saved to: " + path);
            }
        });

        JPanel top = new JPanel(new BorderLayout());
        top.add(revLabel, BorderLayout.WEST);
        top.add(refreshBtn, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(pdfBtn, BorderLayout.SOUTH);
        load.run();
    }
}