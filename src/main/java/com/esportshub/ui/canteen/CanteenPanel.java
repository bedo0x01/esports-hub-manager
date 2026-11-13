package com.esportshub.ui.canteen;

import com.esportshub.core.AppTheme;
import com.esportshub.db.HubDAO;
import com.esportshub.engine.StationTimerService;
import com.esportshub.model.Product;
import com.esportshub.model.Station;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CanteenPanel extends JPanel {
    private JComboBox<Product> prodBox = new JComboBox<>();
    private JComboBox<String> targetBox = new JComboBox<>();
    private JSpinner qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
    private DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Price (EGP)", "In Stock"}, 0);

    public CanteenPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(AppTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.setBackground(AppTheme.BG_CARD);

        JButton orderBtn = new JButton("Add Order");
        JButton refreshBtn = new JButton("Refresh");
        AppTheme.styleButton(orderBtn, AppTheme.GREEN);
        AppTheme.styleButton(refreshBtn, AppTheme.ACCENT_PURPLE);

        JLabel l1 = new JLabel("Item:"); l1.setForeground(AppTheme.TEXT_WHITE);
        JLabel l2 = new JLabel("Qty:"); l2.setForeground(AppTheme.TEXT_WHITE);
        JLabel l3 = new JLabel("Charge To:"); l3.setForeground(AppTheme.TEXT_WHITE);

        top.add(l1); top.add(prodBox);
        top.add(l2); top.add(qtySpinner);
        top.add(l3); top.add(targetBox);
        top.add(orderBtn); top.add(refreshBtn);

        JTable table = new JTable(model);
        AppTheme.styleTable(table);

        refreshBtn.addActionListener(e -> reload());
        orderBtn.addActionListener(e -> {
            Product p = (Product) prodBox.getSelectedItem();
            int qty = (int) qtySpinner.getValue();
            if (p == null || !HubDAO.deductStock(p.id, qty)) {
                JOptionPane.showMessageDialog(this, "Not enough stock!");
                return;
            }
            double total = p.price * qty;
            String target = (String) targetBox.getSelectedItem();
            if (target.equals("Direct Cash")) {
                HubDAO.saveSale("Canteen", "Walk-in", 0, total, total);
            } else {
                int stationId = Integer.parseInt(target.split("-")[0]);
                StationTimerService.getSession(stationId).canteenTotal += total;
            }
            JOptionPane.showMessageDialog(this, "Order Added: " + total + " EGP");
            reload();
        });

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        reload();
    }

    private void reload() {
        prodBox.removeAllItems();
        model.setRowCount(0);
        for (Product p : HubDAO.getAllProducts()) {
            prodBox.addItem(p);
            model.addRow(new Object[]{p.id, p.name, p.price, p.quantity});
        }
        targetBox.removeAllItems();
        targetBox.addItem("Direct Cash");
        for (Station s : HubDAO.getAllStations()) {
            if (StationTimerService.isBusy(s.id)) targetBox.addItem(s.id + "-" + s.name);
        }
    }
}