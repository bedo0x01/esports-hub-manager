package com.esportshub.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:esports_hub.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                role TEXT NOT NULL
            );
        """;

        String createStationsTable = """
            CREATE TABLE IF NOT EXISTS stations (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                status TEXT NOT NULL,
                hourly_rate REAL NOT NULL
            );
        """;

        String createProductsTable = """
            CREATE TABLE IF NOT EXISTS products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                price REAL NOT NULL,
                quantity INTEGER NOT NULL
            );
        """;

        String createSalesTable = """
            CREATE TABLE IF NOT EXISTS sales_history (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                station_name TEXT NOT NULL,
                customer_name TEXT NOT NULL,
                time_cost REAL NOT NULL,
                canteen_cost REAL NOT NULL,
                total_amount REAL NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """;

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createStationsTable);
            stmt.execute(createProductsTable);
            stmt.execute(createSalesTable);
            seedInitialData(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void seedInitialData(Statement stmt) throws SQLException {
        // Seed default users
        stmt.execute("""
            INSERT OR IGNORE INTO users (id, username, password, role) 
            VALUES (1, 'admin', '1234', 'ADMIN'), (2, 'cashier', '1234', 'CASHIER');
        """);

        // Seed default stations if empty
        var rsStations = stmt.executeQuery("SELECT COUNT(*) FROM stations");
        if (rsStations.next() && rsStations.getInt(1) == 0) {
            stmt.execute("""
                INSERT INTO stations (name, status, hourly_rate) VALUES 
                ('PC 1', 'AVAILABLE', 25.0),
                ('PC 2', 'AVAILABLE', 25.0),
                ('PC 3', 'AVAILABLE', 25.0),
                ('PC 4', 'AVAILABLE', 25.0),
                ('PC 5', 'AVAILABLE', 30.0),
                ('PC 6', 'AVAILABLE', 30.0),
                ('PS5 1', 'AVAILABLE', 45.0),
                ('PS5 2', 'AVAILABLE', 45.0);
            """);
        }

        // Seed default products if empty
        var rsProducts = stmt.executeQuery("SELECT COUNT(*) FROM products");
        if (rsProducts.next() && rsProducts.getInt(1) == 0) {
            stmt.execute("""
                INSERT INTO products (name, price, quantity) VALUES 
                ('Pepsi', 15.0, 50),
                ('Red Bull', 45.0, 30),
                ('V-Cola', 15.0, 40),
                ('Chips', 10.0, 35),
                ('Water', 7.0, 60),
                ('Espresso', 25.0, 25);
            """);
        }
    }
}