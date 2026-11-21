package com.esportshub.auth;

import com.esportshub.core.AppTheme;
import com.esportshub.db.HubDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StaffManagementPanel extends JPanel {
    public StaffManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Username", "Role"}, 0);
        JTable table = new JTable(model);

        Runnable refresh = () -> {
            model.setRowCount(0);
            for (Object[] row : HubDAO.getUsers()) model.addRow(row);
        };

        JTextField uField = new JTextField(8);
        JTextField pField = new JTextField(8);
        JComboBox<String> rBox = new JComboBox<>(new String[]{"CASHIER", "ADMIN"});
        JButton addBtn = AppTheme.button("Add", AppTheme.GREEN);
        JButton delBtn = AppTheme.button("Delete Selected", AppTheme.RED);

        addBtn.addActionListener(e -> {
            HubDAO.addUser(uField.getText(), pField.getText(), (String) rBox.getSelectedItem());
            uField.setText(""); pField.setText("");
            refresh.run();
        });

        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                HubDAO.deleteUser((int) model.getValueAt(row, 0));
                refresh.run();
            }
        });

        JPanel top = new JPanel();
        top.add(new JLabel("User:")); top.add(uField);
        top.add(new JLabel("Pass:")); top.add(pField);
        top.add(rBox); top.add(addBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(delBtn, BorderLayout.SOUTH);
        refresh.run();
    }
}