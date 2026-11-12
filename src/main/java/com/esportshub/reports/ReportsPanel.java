package com.esportshub.reports;

import com.esportshub.auth.SessionContext;
import com.esportshub.db.HubDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportsPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable salesTable;
    private JLabel totalRevenueLabel;

    public ReportsPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(18, 21, 28));

        // Top Bar: Title + Total Revenue Card
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel headerLabel = new JLabel("FINANCIAL REPORTS & INVOICES");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(new Color(0, 212, 255));

        totalRevenueLabel = new JLabel("Total Revenue: 0.00 EGP");
        totalRevenueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalRevenueLabel.setForeground(new Color(40, 200, 110));

        topPanel.add(headerLabel, BorderLayout.WEST);
        topPanel.add(totalRevenueLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center: Sales History Table
        String[] columns = {"ID", "Station", "Customer", "Time Cost (EGP)", "Canteen (EGP)", "Total (EGP)", "Timestamp"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        salesTable = new JTable(tableModel);
        salesTable.setRowHeight(28);
        salesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        JScrollPane scrollPane = new JScrollPane(salesTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom: Action Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setOpaque(false);

        JButton refreshBtn = new JButton("Refresh Data");
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> loadReportsData());

        JButton exportPdfBtn = new JButton("Export Selected Invoice to PDF");
        exportPdfBtn.setBackground(new Color(0, 150, 255));
        exportPdfBtn.setForeground(Color.WHITE);
        exportPdfBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        exportPdfBtn.setFocusPainted(false);
        exportPdfBtn.addActionListener(e -> exportSelectedInvoice());

        bottomPanel.add(refreshBtn);
        bottomPanel.add(exportPdfBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        loadReportsData();
    }

    public void loadReportsData() {
        tableModel.setRowCount(0);
        List<Object[]> records = HubDAO.getAllSalesRecords();
        for (Object[] row : records) {
            tableModel.addRow(row);
        }

        if (SessionContext.isAdmin()) {
            double total = HubDAO.getTotalRevenue();
            totalRevenueLabel.setText(String.format("Total Revenue: %.2f EGP", total));
        } else {
            totalRevenueLabel.setText("Total Revenue: [Admin Only]");
            totalRevenueLabel.setForeground(Color.GRAY);
        }
    }

    private void exportSelectedInvoice() {
        int selectedRow = salesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an invoice row from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = tableModel.getValueAt(selectedRow, 0).toString();
        String station = tableModel.getValueAt(selectedRow, 1).toString();
        String customer = tableModel.getValueAt(selectedRow, 2).toString();
        String timeCost = tableModel.getValueAt(selectedRow, 3).toString();
        String canteenCost = tableModel.getValueAt(selectedRow, 4).toString();
        String total = tableModel.getValueAt(selectedRow, 5).toString();

        String pdfPath = PdfInvoiceGenerator.generateInvoicePdf(id, station, customer, timeCost, canteenCost, total);
        if (pdfPath != null) {
            JOptionPane.showMessageDialog(this,
                    "PDF Invoice exported successfully!\nSaved at:\n" + pdfPath,
                    "PDF Generated", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to generate PDF invoice.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}