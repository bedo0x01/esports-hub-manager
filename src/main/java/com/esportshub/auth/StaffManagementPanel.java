package com.esportshub.auth;

import com.esportshub.db.HubDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StaffManagementPanel extends JPanel {
    private JTextField userField;
    private JPasswordField passField;
    private JComboBox<String> roleBox;
    private DefaultTableModel tableModel;
    private JTable staffTable;

    public StaffManagementPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(18, 21, 28));

        JLabel title = new JLabel("STAFF & PERMISSIONS MANAGEMENT");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(0, 212, 255));
        add(title, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(1, 2, 20, 0));
        body.setOpaque(false);
        body.add(buildAddStaffForm());
        body.add(buildStaffTable());

        add(body, BorderLayout.CENTER);
        reloadStaffTable();
    }

    private JPanel buildAddStaffForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(28, 33, 44));
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 52, 68), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);

        JLabel formTitle = new JLabel("Register New Employee");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        form.add(formTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel uLbl = new JLabel("Username:");
        uLbl.setForeground(Color.LIGHT_GRAY);
        form.add(uLbl, gbc);

        gbc.gridx = 1;
        userField = new JTextField(15);
        form.add(userField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel pLbl = new JLabel("Password:");
        pLbl.setForeground(Color.LIGHT_GRAY);
        form.add(pLbl, gbc);

        gbc.gridx = 1;
        passField = new JPasswordField(15);
        form.add(passField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel rLbl = new JLabel("Access Role:");
        rLbl.setForeground(Color.LIGHT_GRAY);
        form.add(rLbl, gbc);

        gbc.gridx = 1;
        roleBox = new JComboBox<>(new String[]{"CASHIER", "ADMIN"});
        form.add(roleBox, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        JButton createBtn = new JButton("Create Account");
        createBtn.setBackground(new Color(40, 167, 69));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createBtn.setPreferredSize(new Dimension(0, 40));
        createBtn.setFocusPainted(false);
        createBtn.addActionListener(e -> handleCreateUser());
        form.add(createBtn, gbc);

        return form;
    }

    private JPanel buildStaffTable() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(28, 33, 44));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(45, 52, 68), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel tblTitle = new JLabel("Active System Accounts");
        tblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tblTitle.setForeground(Color.WHITE);
        panel.add(tblTitle, BorderLayout.NORTH);

        String[] cols = {"ID", "Username", "Role"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        staffTable = new JTable(tableModel);
        staffTable.setRowHeight(28);
        panel.add(new JScrollPane(staffTable), BorderLayout.CENTER);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnBar.setOpaque(false);

        JButton resetPassBtn = new JButton("Change Password");
        resetPassBtn.setFocusPainted(false);
        resetPassBtn.addActionListener(e -> handleChangePassword());

        JButton removeBtn = new JButton("Remove Account");
        removeBtn.setBackground(new Color(220, 53, 69));
        removeBtn.setForeground(Color.WHITE);
        removeBtn.setFocusPainted(false);
        removeBtn.addActionListener(e -> handleDeleteUser());

        btnBar.add(resetPassBtn);
        btnBar.add(removeBtn);
        panel.add(btnBar, BorderLayout.SOUTH);

        return panel;
    }

    private void reloadStaffTable() {
        tableModel.setRowCount(0);
        List<Object[]> list = HubDAO.getAllUsers();
        for (Object[] row : list) {
            tableModel.addRow(row);
        }
    }

    private void handleCreateUser() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();
        String role = (String) roleBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.");
            return;
        }

        if (HubDAO.addUser(username, password, role)) {
            JOptionPane.showMessageDialog(this, "User '" + username + "' added as " + role + ".");
            userField.setText("");
            passField.setText("");
            reloadStaffTable();
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleChangePassword() {
        int row = staffTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a user from the table first.");
            return;
        }

        int userId = (int) tableModel.getValueAt(row, 0);
        String username = (String) tableModel.getValueAt(row, 1);
        String newPass = JOptionPane.showInputDialog(this, "Enter new password for " + username + ":");

        if (newPass != null && !newPass.trim().isEmpty()) {
            if (HubDAO.updateUserPassword(userId, newPass.trim())) {
                JOptionPane.showMessageDialog(this, "Password updated successfully.");
            }
        }
    }

    private void handleDeleteUser() {
        int row = staffTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a user from the table first.");
            return;
        }

        int userId = (int) tableModel.getValueAt(row, 0);
        String username = (String) tableModel.getValueAt(row, 1);

        if (userId == 1) {
            JOptionPane.showMessageDialog(this, "Cannot delete the main system administrator.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete user '" + username + "'?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && HubDAO.deleteUser(userId)) {
            reloadStaffTable();
        }
    }
}