package com.esportshub.ui;

import com.esportshub.db.Database;
import com.esportshub.engine.SessionEngine;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CanteenPanel extends JPanel {
    public CanteenPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JComboBox<String> itemBox = new JComboBox<>();
        JComboBox<String> targetBox = new JComboBox<>();
        JSpinner qtySpin = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Item", "Price", "Stock"}, 0);

        Runnable reload = () -> {
            itemBox.removeAllItems(); model.setRowCount(0);
            for (Object[] p : Database.query("SELECT * FROM products")) {
                itemBox.addItem(p[0] + "-" + p[1] + "-" + p[2]);
                model.addRow(p);
            }
            targetBox.removeAllItems();
            targetBox.addItem("Cash");
            for (Object[] st : Database.query("SELECT id, name FROM stations"))
                if (SessionEngine.isBusy((int) st[0])) targetBox.addItem(st[0] + "-" + st[1]);
        };

        JButton orderBtn = new JButton("Order");
        JButton refBtn = new JButton("Refresh");
        refBtn.addActionListener(e -> reload.run());

        orderBtn.addActionListener(e -> {
            String[] parts = itemBox.getSelectedItem().toString().split("-");
            int id = Integer.parseInt(parts[0]);
            double price = Double.parseDouble(parts[2]);
            int q = (int) qtySpin.getValue();

            if (!Database.execute("UPDATE products SET qty = qty - ? WHERE id=? AND qty >= ?", q, id, q)) {
                JOptionPane.showMessageDialog(this, "Out of stock!");
                return;
            }
            double cost = price * q;
            String target = targetBox.getSelectedItem().toString();
            if ("Cash".equals(target)) {
                Database.execute("INSERT INTO sales (station,customer,time_cost,canteen_cost,total) VALUES ('Canteen','Walk-in',0,?,?)", cost, cost);
            } else {
                SessionEngine.active.get(Integer.parseInt(target.split("-")[0])).canteen += cost;
            }
            JOptionPane.showMessageDialog(this, "Added: " + cost + " EGP");
            reload.run();
        });

        JPanel top = new JPanel();
        top.add(itemBox); top.add(qtySpin); top.add(targetBox); top.add(orderBtn); top.add(refBtn);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
        reload.run();
    }
}
