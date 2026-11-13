package com.esportshub.db;

import java.sql.*;

public class DatabaseManager {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:esports_hub.db");
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, role TEXT)");
            st.execute("CREATE TABLE IF NOT EXISTS stations (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, status TEXT, hourly_rate REAL)");
            st.execute("CREATE TABLE IF NOT EXISTS products (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, quantity INTEGER)");
            st.execute("CREATE TABLE IF NOT EXISTS sales_history (id INTEGER PRIMARY KEY AUTOINCREMENT, station_name TEXT, customer_name TEXT, time_cost REAL, canteen_cost REAL, total_amount REAL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            st.execute("INSERT OR IGNORE INTO users (id, username, password, role) VALUES (1, 'admin', '1234', 'ADMIN')");
            st.execute("INSERT OR IGNORE INTO users (id, username, password, role) VALUES (2, 'cashier', '1234', 'CASHIER')");

            ResultSet rs1 = st.executeQuery("SELECT COUNT(*) FROM stations");
            if (rs1.next() && rs1.getInt(1) == 0) {
                st.execute("INSERT INTO stations (name, status, hourly_rate) VALUES ('PC 1', 'AVAILABLE', 25), ('PC 2', 'AVAILABLE', 25), ('PC 3', 'AVAILABLE', 30), ('PC 4', 'AVAILABLE', 30), ('PS5 1', 'AVAILABLE', 45), ('PS5 2', 'AVAILABLE', 45)");
            }

            ResultSet rs2 = st.executeQuery("SELECT COUNT(*) FROM products");
            if (rs2.next() && rs2.getInt(1) == 0) {
                st.execute("INSERT INTO products (name, price, quantity) VALUES ('Pepsi', 15, 50), ('Red Bull', 45, 30), ('Chips', 10, 40), ('Water', 7, 60)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}