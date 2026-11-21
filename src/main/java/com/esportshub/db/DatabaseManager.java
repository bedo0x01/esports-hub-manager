package com.esportshub.db;

import java.sql.*;

public class DatabaseManager {
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:esports_hub.db");
    }

    public static void init() {
        try (Connection c = connect(); Statement s = c.createStatement()) {
            // إنشاء الجداول بالأعمدة الجديدة
            s.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, role TEXT)");
            s.execute("CREATE TABLE IF NOT EXISTS stations (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, rate REAL)");
            s.execute("CREATE TABLE IF NOT EXISTS products (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, qty INTEGER)");
            s.execute("CREATE TABLE IF NOT EXISTS sales (id INTEGER PRIMARY KEY AUTOINCREMENT, station TEXT, customer TEXT, time_cost REAL, canteen_cost REAL, total REAL)");

            // لو الجداول كانت مبنية بالأعمدة القديمة، نعيد بناء stations و products
            try {
                s.executeQuery("SELECT rate FROM stations LIMIT 1");
            } catch (SQLException e) {
                s.execute("DROP TABLE IF EXISTS stations");
                s.execute("CREATE TABLE stations (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, rate REAL)");
            }

            try {
                s.executeQuery("SELECT qty FROM products LIMIT 1");
            } catch (SQLException e) {
                s.execute("DROP TABLE IF EXISTS products");
                s.execute("CREATE TABLE products (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, qty INTEGER)");
            }

            try {
                s.executeQuery("SELECT station FROM sales LIMIT 1");
            } catch (SQLException e) {
                s.execute("DROP TABLE IF EXISTS sales");
                s.execute("CREATE TABLE sales (id INTEGER PRIMARY KEY AUTOINCREMENT, station TEXT, customer TEXT, time_cost REAL, canteen_cost REAL, total REAL)");
            }

            // إضافة البيانات الافتراضية
            s.execute("INSERT OR IGNORE INTO users VALUES (1,'admin','1234','ADMIN'), (2,'cashier','1234','CASHIER')");

            if (s.executeQuery("SELECT COUNT(*) FROM stations").getInt(1) == 0)
                s.execute("INSERT INTO stations (name,rate) VALUES ('PC 1',25),('PC 2',25),('PC 3',30),('PS5 1',45),('PS5 2',45)");

            if (s.executeQuery("SELECT COUNT(*) FROM products").getInt(1) == 0)
                s.execute("INSERT INTO products (name,price,qty) VALUES ('Pepsi',15,50),('Red Bull',45,30),('Chips',10,40)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}