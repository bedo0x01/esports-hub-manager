package com.esportshub.db;

import java.sql.*;
import java.util.*;

public class Database {
    public static String currentUser = "";
    public static String currentRole = "";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:esports_hub.db");
    }

    public static void init() {
        try (Connection c = connect(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, role TEXT)");
            s.execute("CREATE TABLE IF NOT EXISTS stations (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, rate REAL)");
            s.execute("CREATE TABLE IF NOT EXISTS products (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, qty INTEGER)");
            s.execute("CREATE TABLE IF NOT EXISTS sales (id INTEGER PRIMARY KEY AUTOINCREMENT, station TEXT, customer TEXT, time_cost REAL, canteen_cost REAL, total REAL)");

            s.execute("INSERT OR IGNORE INTO users VALUES (1,'admin','1234','ADMIN'), (2,'cashier','1234','CASHIER')");

            if (s.executeQuery("SELECT COUNT(*) FROM stations").getInt(1) == 0)
                s.execute("INSERT INTO stations (name,rate) VALUES ('PC 1',25),('PC 2',25),('PC 3',30),('PS5 1',45),('PS5 2',45)");

            if (s.executeQuery("SELECT COUNT(*) FROM products").getInt(1) == 0)
                s.execute("INSERT INTO products (name,price,qty) VALUES ('Pepsi',15,50),('Red Bull',45,30),('Chips',10,40)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean login(String user, String pass) {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement("SELECT role FROM users WHERE username=? AND password=?")) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                currentUser = user;
                currentRole = rs.getString("role");
                return true;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public static List<Object[]> query(String sql) {
        List<Object[]> rows = new ArrayList<>();
        try (Connection c = connect(); ResultSet rs = c.createStatement().executeQuery(sql)) {
            int cols = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] r = new Object[cols];
                for (int i = 0; i < cols; i++) r[i] = rs.getObject(i + 1);
                rows.add(r);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return rows;
    }

    public static boolean execute(String sql, Object... params) {
        try (Connection c = connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }
}
