package com.esportshub.ui;

import com.esportshub.db.Database;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class ReportsPanel extends JPanel {
    public ReportsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Station", "Player", "Time", "Canteen", "Total"}, 0);
        JTable table = new JTable(model);
        JLabel revLbl = new JLabel();

        Runnable load = () -> {
            model.setRowCount(0);
            for (Object[] r : Database.query("SELECT * FROM sales ORDER BY id DESC")) model.addRow(r);
            Object sum = Database.query("SELECT COALESCE(SUM(total),0) FROM sales").get(0)[0];
            revLbl.setText("ADMIN".equals(Database.currentRole) ? "Total Revenue: " + sum + " EGP" : "Revenue: Admin Only");
        };

        JButton refBtn = new JButton("Refresh");
        JButton pdfBtn = new JButton("Print PDF");
        JButton staffBtn = new JButton("Add Cashier");

        refBtn.addActionListener(e -> load.run());
        pdfBtn.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) return;
            try {
                new File("invoices").mkdirs();
                String file = "invoices/Invoice_" + model.getValueAt(r, 0) + ".pdf";
                Document doc = new Document(new PdfDocument(new PdfWriter(file)));
                doc.add(new Paragraph("ESPORTS HUB RECEIPT #" + model.getValueAt(r, 0) + "\nStation: " + model.getValueAt(r, 1) + "\nPlayer: " + model.getValueAt(r, 2) + "\nTotal: " + model.getValueAt(r, 5) + " EGP"));
                doc.close();
                JOptionPane.showMessageDialog(this, "Saved: " + file);
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        staffBtn.addActionListener(e -> {
            String u = JOptionPane.showInputDialog(this, "New Cashier Username:");
            String p = JOptionPane.showInputDialog(this, "Password:");
            if (u != null && p != null && Database.execute("INSERT INTO users (username,password,role) VALUES (?,?,'CASHIER')", u, p))
                JOptionPane.showMessageDialog(this, "Cashier Added!");
        });

        JPanel top = new JPanel();
        top.add(revLbl); top.add(refBtn); top.add(pdfBtn);
        if ("ADMIN".equals(Database.currentRole)) top.add(staffBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        load.run();
    }
}
