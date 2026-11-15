package com.esportshub.db;

import com.esportshub.model.*;
import java.sql.*;
import java.util.*;

// Data Access Object (DAO) for all database CRUD operations
public class HubDAO {

    // Generic helper to execute SELECT queries and return rows
    private static List<Object[]> query(String sql) {
        List<Object[]> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection(); ResultSet rs = c.createStatement().executeQuery(sql)) {
            int cols = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[cols];
                for (int i = 0; i < cols; i++) row[i] = rs.getObject(i + 1);
                list.add(row);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Generic helper using PreparedStatement to prevent SQL Injection
    private static boolean exec(String sql, Object... args) {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) ps.setObject(i + 1, args[i]);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    // Authenticates user credentials and returns role (ADMIN / CASHIER)
    public static String login(String u, String p) {
        try (Connection c = DatabaseManager.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT role FROM users WHERE username=? AND password=?")) {
            ps.setString(1, u); ps.setString(2, p);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : null;
        } catch (Exception e) { return null; }
    }

    // Staff account management methods
    public static List<Object[]> getAllUsers() { return query("SELECT id, username, role FROM users"); }
    public static boolean addUser(String u, String p, String r) { return exec("INSERT INTO users (username, password, role) VALUES (?,?,?)", u, p, r); }
    public static void deleteUser(int id) { if (id != 1) exec("DELETE FROM users WHERE id=?", id); }

    // Loads all stations from database
    public static List<Station> getAllStations() {
        List<Station> list = new ArrayList<>();
        for (Object[] r : query("SELECT * FROM stations"))
            list.add(new Station((int) r[0], (String) r[1], (String) r[2], ((Number) r[3]).doubleValue()));
        return list;
    }

    // Loads all canteen products from database
    public static List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        for (Object[] r : query("SELECT * FROM products"))
            list.add(new Product((int) r[0], (String) r[1], ((Number) r[2]).doubleValue(), (int) r[3]));
        return list;
    }

    // Deducts sold quantity safely only if stock is sufficient
    public static boolean deductStock(int id, int qty) { return exec("UPDATE products SET quantity = quantity - ? WHERE id=? AND quantity >= ?", qty, id, qty); }

    // Saves completed invoice into sales_history
    public static void saveSale(String st, String cust, double t, double c, double tot) { exec("INSERT INTO sales_history (station_name, customer_name, time_cost, canteen_cost, total_amount) VALUES (?,?,?,?,?)", st, cust, t, c, tot); }

    // Fetches all sales records ordered from newest to oldest
    public static List<Object[]> getAllSales() { return query("SELECT id, station_name, customer_name, ROUND(time_cost,2), ROUND(canteen_cost,2), ROUND(total_amount,2), created_at FROM sales_history ORDER BY id DESC"); }

    // Calculates total revenue across all sales
    public static double getTotalRevenue() { return ((Number) query("SELECT COALESCE(SUM(total_amount),0) FROM sales_history").get(0)[0]).doubleValue(); }
}