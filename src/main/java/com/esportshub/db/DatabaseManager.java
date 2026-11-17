package com.esportshub.db;

import java.sql.*;

// Manages SQLite connection and initial table creation
public class DatabaseManager {
    // Opens connection to local SQLite file
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:esports_hub.db");
    }

    // Creates database tables and seeds default users, stations, and products
    public static void initializeDatabase() {
        try (Connection c = getConnection(); Statement st = c.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, role TEXT)");
            st.execute("CREATE TABLE IF NOT EXISTS stations (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, status TEXT, hourly_rate REAL)");
            st.execute("CREATE TABLE IF NOT EXISTS products (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, quantity INTEGER)");
            st.execute("CREATE TABLE IF NOT EXISTS sales_history (id INTEGER PRIMARY KEY AUTOINCREMENT, station_name TEXT, customer_name TEXT, time_cost REAL, canteen_cost REAL, total_amount REAL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            st.execute("INSERT OR IGNORE INTO users VALUES (1, 'admin', '1234', 'ADMIN'), (2, 'cashier', '1234', 'CASHIER')");

            if (st.executeQuery("SELECT COUNT(*) FROM stations").getInt(1) == 0) {
                st.execute("INSERT INTO stations (name, status, hourly_rate) VALUES ('PC 1','AVAILABLE',25),('PC 2','AVAILABLE',25),('PC 3','AVAILABLE',30),('PC 4','AVAILABLE',30),('PS5 1','AVAILABLE',45),('PS5 2','AVAILABLE',45)");
            }
            if (st.executeQuery("SELECT COUNT(*) FROM products").getInt(1) == 0) {
                st.execute("INSERT INTO products (name, price, quantity) VALUES ('Pepsi',15,50),('Red Bull',45,30),('Chips',10,40),('Water',7,60)('noodles',30,60)");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}