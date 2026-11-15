package com.esportshub.ui.canteen;

import com.esportshub.core.AppTheme;
import com.esportshub.db.HubDAO;
import com.esportshub.engine.StationTimerService;
import com.esportshub.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Point of Sale (POS) panel for direct cash sales or charging items to active stations
public class CanteenPanel extends JPanel {
    public CanteenPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(AppTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<Product> pBox = new JComboBox<>();
        JComboBox<String> tBox = new JComboBox<>();
        JSpinner qty = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Stock"}, 0);

        // Refreshes product stock table and list of currently busy stations
        Runnable reload = () -> {
            pBox.removeAllItems(); model.setRowCount(0);
            for (Product p : HubDAO.getAllProducts()) { pBox.addItem(p); model.addRow(new Object[]{p.id, p.name, p.price, p.quantity}); }
            tBox.removeAllItems(); tBox.addItem("Direct Cash");
            for (Station s : HubDAO.getAllStations()) if (StationTimerService.isBusy(s.id)) tBox.addItem(s.id + "-" + s.name);
        };

        JButton add = AppTheme.btn("Add Order", AppTheme.GREEN), ref = AppTheme.btn("Refresh", AppTheme.PURPLE);
        ref.addActionListener(e -> reload.run());

        // Deducts stock and either saves cash sale or adds cost to active station tab
        add.addActionListener(e -> {
            Product p = (Product) pBox.getSelectedItem();
            int q = (int) qty.getValue();
            if (p == null || !HubDAO.deductStock(p.id, q)) { JOptionPane.showMessageDialog(this, "Out of stock!"); return; }
            double tot = p.price * q;
            String target = (String) tBox.getSelectedItem();
            if ("Direct Cash".equals(target)) HubDAO.saveSale("Canteen", "Walk-in", 0, tot, tot);
            else StationTimerService.getSession(Integer.parseInt(target.split("-")[0])).canteenTotal += tot;
            JOptionPane.showMessageDialog(this, "Added: " + tot + " EGP");
            reload.run();
        });

        JPanel top = new JPanel(); top.setBackground(AppTheme.BG_CARD);
        top.add(pBox); top.add(qty); top.add(tBox); top.add(add); top.add(ref);
        add(top, BorderLayout.NORTH); add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
        reload.run();
    }
}