package com.esportshub.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:esports_hub.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String usersSql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL, "
                + "role TEXT NOT NULL)";

        String stationsSql = "CREATE TABLE IF NOT EXISTS stations ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "status TEXT NOT NULL, "
                + "hourly_rate REAL NOT NULL)";

        String productsSql = "CREATE TABLE IF NOT EXISTS products ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "price REAL NOT NULL, "
                + "quantity INTEGER NOT NULL)";

        String salesSql = "CREATE TABLE IF NOT EXISTS sales_history ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "station_name TEXT NOT NULL, "
                + "customer_name TEXT NOT NULL, "
                + "time_cost REAL NOT NULL, "
                + "canteen_cost REAL NOT NULL, "
                + "total_amount REAL NOT NULL, "
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute(usersSql);
            st.execute(stationsSql);
            st.execute(productsSql);
            st.execute(salesSql);
            insertDefaultRecords(st);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void insertDefaultRecords(Statement st) throws SQLException {
        st.execute("INSERT OR IGNORE INTO users (id, username, password, role) VALUES (1, 'admin', '1234', 'ADMIN')");
        st.execute("INSERT OR IGNORE INTO users (id, username, password, role) VALUES (2, 'cashier', '1234', 'CASHIER')");

        ResultSet rsStations = st.executeQuery("SELECT COUNT(*) FROM stations");
        if (rsStations.next() && rsStations.getInt(1) == 0) {
            st.execute("INSERT INTO stations (name, status, hourly_rate) VALUES "
                    + "('PC 1', 'AVAILABLE', 25.0), ('PC 2', 'AVAILABLE', 25.0), "
                    + "('PC 3', 'AVAILABLE', 25.0), ('PC 4', 'AVAILABLE', 25.0), "
                    + "('PC 5', 'AVAILABLE', 30.0), ('PC 6', 'AVAILABLE', 30.0), "
                    + "('PS5 1', 'AVAILABLE', 45.0), ('PS5 2', 'AVAILABLE', 45.0)");
        }

        ResultSet rsProducts = st.executeQuery("SELECT COUNT(*) FROM products");
        if (rsProducts.next() && rsProducts.getInt(1) == 0) {
            st.execute("INSERT INTO products (name, price, quantity) VALUES "
                    + "('Pepsi', 15.0, 50), ('Red Bull', 45.0, 30), "
                    + "('V-Cola', 15.0, 40), ('Chips', 10.0, 35), "
                    + "('Water', 7.0, 60), ('Espresso', 25.0, 25)");
        }
    }
}