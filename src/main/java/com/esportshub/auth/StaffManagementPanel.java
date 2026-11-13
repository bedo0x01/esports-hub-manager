package com.esportshub.auth;

import com.esportshub.core.AppTheme;
import com.esportshub.db.HubDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StaffManagementPanel extends JPanel {
    private DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Username", "Role"}, 0);
    private JTable table = new JTable(model);

    public StaffManagementPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(AppTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topForm.setBackground(AppTheme.BG_CARD);

        JLabel l1 = new JLabel("User:");
        l1.setForeground(AppTheme.TEXT_WHITE);
        JLabel l2 = new JLabel("Pass:");
        l2.setForeground(AppTheme.TEXT_WHITE);
        JLabel l3 = new JLabel("Role:");
        l3.setForeground(AppTheme.TEXT_WHITE);

        JTextField userField = new JTextField(10);
        JTextField passField = new JTextField(10);
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"CASHIER", "ADMIN"});
        JButton addBtn = new JButton("Add Staff");
        AppTheme.styleButton(addBtn, AppTheme.GREEN);

        topForm.add(l1);
        topForm.add(userField);
        topForm.add(l2);
        topForm.add(passField);
        topForm.add(l3);
        topForm.add(roleBox);
        topForm.add(addBtn);

        AppTheme.styleTable(table);

        JButton deleteBtn = new JButton("Delete Selected User");
        AppTheme.styleButton(deleteBtn, AppTheme.RED);

        addBtn.addActionListener(e -> {
            if (HubDAO.addUser(userField.getText(), passField.getText(), (String) roleBox.getSelectedItem())) {
                userField.setText("");
                passField.setText("");
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists!");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                HubDAO.deleteUser((int) model.getValueAt(row, 0));
                refresh();
            }
        });

        add(topForm, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(deleteBtn, BorderLayout.SOUTH);
        refresh();
    }

    private void refresh() {
        model.setRowCount(0);
        for (Object[] row : HubDAO.getAllUsers()) model.addRow(row);
    }
}