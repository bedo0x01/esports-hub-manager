package com.esportshub.ui.canteen;

import com.esportshub.core.AppTheme;
import com.esportshub.db.HubDAO;
import com.esportshub.engine.StationTimerService;
import com.esportshub.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CanteenPanel extends JPanel {
    public CanteenPanel() {
        setLayout(new BorderLayout(10, 10));
        JComboBox<Product> itemBox = new JComboBox<>();
        JComboBox<String> targetBox = new JComboBox<>();
        JSpinner qtySpin = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Item", "Price", "Stock"}, 0);

        Runnable reload = () -> {
            itemBox.removeAllItems(); model.setRowCount(0);
            for (Product p : HubDAO.getProducts()) {
                itemBox.addItem(p);
                model.addRow(new Object[]{p.id, p.name, p.price, p.qty});
            }
            targetBox.removeAllItems();
            targetBox.addItem("Cash");
            for (Station s : HubDAO.getStations())
                if (StationTimerService.isBusy(s.id)) targetBox.addItem(s.id + "-" + s.name);
        };

        JButton buyBtn = AppTheme.button("Order", AppTheme.GREEN);
        JButton refBtn = AppTheme.button("Refresh", AppTheme.PURPLE);
        refBtn.addActionListener(e -> reload.run());

        buyBtn.addActionListener(e -> {
            Product p = (Product) itemBox.getSelectedItem();
            int q = (int) qtySpin.getValue();
            if (p == null || !HubDAO.buyProduct(p.id, q)) {
                JOptionPane.showMessageDialog(this, "Out of stock!");
                return;
            }
            double cost = p.price * q;
            String target = (String) targetBox.getSelectedItem();
            if ("Cash".equals(target)) HubDAO.addSale("Canteen", "Walk-in", 0, cost);
            else StationTimerService.active.get(Integer.parseInt(target.split("-")[0])).canteenCost += cost;

            JOptionPane.showMessageDialog(this, "Ordered: " + cost + " EGP");
            reload.run();
        });

        JPanel top = new JPanel();
        top.add(itemBox); top.add(qtySpin); top.add(targetBox); top.add(buyBtn); top.add(refBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
        reload.run();
    }
}