package com.esportshub.ui.canteen;

import com.esportshub.db.HubDAO;
import com.esportshub.engine.StationTimerService;
import com.esportshub.model.Product;
import com.esportshub.model.Station;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CanteenPanel extends JPanel {
    private final StationTimerService timerService = StationTimerService.getInstance();
    private JComboBox<Product> productComboBox;
    private JComboBox<String> targetComboBox;
    private JSpinner quantitySpinner;
    private DefaultTableModel inventoryTableModel;

    public CanteenPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(18, 21, 28));

        // Top Header
        JLabel headerLabel = new JLabel("CANTEEN & SNACKS POS");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(new Color(0, 212, 255));
        add(headerLabel, BorderLayout.NORTH);

        // Split Layout: Left = Order Form, Right = Live Inventory Table
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContent.setOpaque(false);

        mainContent.add(createOrderFormPanel());
        mainContent.add(createInventoryTablePanel());

        add(mainContent, BorderLayout.CENTER);
        refreshData();
    }

    private JPanel createOrderFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(28, 33, 44));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 52, 68), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);

        JLabel title = new JLabel("New Canteen Order");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(title, gbc);

        // Select Product
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel prodLabel = new JLabel("Select Item:");
        prodLabel.setForeground(Color.LIGHT_GRAY);
        formPanel.add(prodLabel, gbc);

        gbc.gridx = 1;
        productComboBox = new JComboBox<>();
        formPanel.add(productComboBox, gbc);

        // Select Quantity
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setForeground(Color.LIGHT_GRAY);
        formPanel.add(qtyLabel, gbc);

        gbc.gridx = 1;
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        formPanel.add(quantitySpinner, gbc);

        // Assign To (Station or Direct Cash Takeaway)
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel targetLabel = new JLabel("Charge To:");
        targetLabel.setForeground(Color.LIGHT_GRAY);
        formPanel.add(targetLabel, gbc);

        gbc.gridx = 1;
        targetComboBox = new JComboBox<>();
        formPanel.add(targetComboBox, gbc);

        // Action Buttons
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);

        JButton addOrderBtn = new JButton("Confirm & Add Order");
        addOrderBtn.setBackground(new Color(40, 167, 69));
        addOrderBtn.setForeground(Color.WHITE);
        addOrderBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addOrderBtn.setPreferredSize(new Dimension(0, 42));
        addOrderBtn.setFocusPainted(false);
        addOrderBtn.addActionListener(e -> processOrder());
        formPanel.add(addOrderBtn, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(5, 5, 5, 5);
        JButton refreshBtn = new JButton("Refresh Active Stations & Stock");
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> refreshData());
        formPanel.add(refreshBtn, gbc);

        return formPanel;
    }

    private JPanel createInventoryTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(new Color(28, 33, 44));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 52, 68), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel title = new JLabel("Current Stock & Prices");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        tablePanel.add(title, BorderLayout.NORTH);

        String[] columns = {"ID", "Product Name", "Unit Price (EGP)", "In Stock"};
        inventoryTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(inventoryTableModel);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    public void refreshData() {
        // Reload products in ComboBox and Table
        productComboBox.removeAllItems();
        inventoryTableModel.setRowCount(0);
        List<Product> products = HubDAO.getAllProducts();
        for (Product p : products) {
            productComboBox.addItem(p);
            inventoryTableModel.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getQuantity()});
        }

        // Reload active stations in Target ComboBox
        targetComboBox.removeAllItems();
        targetComboBox.addItem("Direct Cash (Walk-in)");
        List<Station> stations = HubDAO.getAllStations();
        for (Station s : stations) {
            if (timerService.isStationBusy(s.getId())) {
                targetComboBox.addItem(s.getId() + " - " + s.getName() + " (Active)");
            }
        }
    }

    private void processOrder() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        if (selectedProduct == null) return;

        int qty = (int) quantitySpinner.getValue();
        double totalOrderPrice = selectedProduct.getPrice() * qty;
        String target = (String) targetComboBox.getSelectedItem();

        if (selectedProduct.getQuantity() < qty) {
            JOptionPane.showMessageDialog(this, "Insufficient stock available!", "Stock Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Deduct from SQLite inventory
        boolean stockUpdated = HubDAO.deductProductStock(selectedProduct.getId(), qty);
        if (!stockUpdated) {
            JOptionPane.showMessageDialog(this, "Failed to update inventory.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (target != null && target.startsWith("Direct Cash")) {
            // Save immediately as walk-in sale
            HubDAO.saveSaleRecord("Canteen Walk-in", "Walk-in Customer", 0.0, totalOrderPrice, totalOrderPrice);
            JOptionPane.showMessageDialog(this,
                    String.format("Walk-in sale recorded!\nItem: %s x%d\nTotal Paid: %.2f EGP",
                            selectedProduct.getName(), qty, totalOrderPrice),
                    "Order Completed", JOptionPane.INFORMATION_MESSAGE);
        } else if (target != null) {
            // Extract Station ID and add charge to live session
            int stationId = Integer.parseInt(target.split(" - ")[0].trim());
            boolean added = timerService.addOrderToStation(stationId, totalOrderPrice);
            if (added) {
                JOptionPane.showMessageDialog(this,
                        String.format("Added %.2f EGP (%s x%d) to Station #%d bill!",
                                totalOrderPrice, selectedProduct.getName(), qty, stationId),
                        "Added to Station", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Station session ended before order could be added.", "Session Closed", JOptionPane.WARNING_MESSAGE);
            }
        }

        quantitySpinner.setValue(1);
        refreshData();
    }
}