package com.esportshub.auth;

import com.esportshub.core.AppTheme;
import com.esportshub.db.HubDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Admin-only panel to create or remove CASHIER/ADMIN accounts
public class StaffManagementPanel extends JPanel {
    private DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Username", "Role"}, 0);

    public StaffManagementPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(AppTheme.BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField u = new JTextField(10), p = new JTextField(10);
        JComboBox<String> rBox = new JComboBox<>(new String[]{"CASHIER", "ADMIN"});
        JButton add = AppTheme.btn("Add Staff", AppTheme.GREEN), del = AppTheme.btn("Delete Selected", AppTheme.RED);
        JTable table = new JTable(model);

        // Reloads user table from database
        Runnable load = () -> { model.setRowCount(0); for (Object[] r : HubDAO.getAllUsers()) model.addRow(r); };

        // Adds new staff user with selected role
        add.addActionListener(e -> { if (HubDAO.addUser(u.getText(), p.getText(), (String) rBox.getSelectedItem())) { u.setText(""); p.setText(""); load.run(); } });

        // Deletes selected user (main admin id=1 is protected in HubDAO)
        del.addActionListener(e -> { if (table.getSelectedRow() != -1) { HubDAO.deleteUser((int) model.getValueAt(table.getSelectedRow(), 0)); load.run(); } });

        JPanel top = new JPanel(); top.setBackground(AppTheme.BG_CARD);
        top.add(AppTheme.lbl("User:", Color.WHITE)); top.add(u);
        top.add(AppTheme.lbl("Pass:", Color.WHITE)); top.add(p);
        top.add(rBox); top.add(add);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(del, BorderLayout.SOUTH);
        load.run();
    }
}