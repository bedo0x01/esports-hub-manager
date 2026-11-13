package com.esportshub.db;

import com.esportshub.model.Product;
import com.esportshub.model.Station;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HubDAO {

    public static String login(String user, String pass) {
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT role FROM users WHERE username=? AND password=?")) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("role");
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public static List<Object[]> getAllUsers() {
        List<Object[]> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT id, username, role FROM users")) {
            while (rs.next()) {
                list.add(new Object[]{rs.getInt("id"), rs.getString("username"), rs.getString("role")});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public static boolean addUser(String user, String pass, String role) {
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)")) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ps.setString(3, role);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    public static void deleteUser(int id) {
        if (id == 1) return;
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static List<Station> getAllStations() {
        List<Station> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM stations")) {
            while (rs.next()) {
                list.add(new Station(rs.getInt("id"), rs.getString("name"), rs.getString("status"), rs.getDouble("hourly_rate")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public static List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM products")) {
            while (rs.next()) {
                list.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("quantity")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public static boolean deductStock(int id, int qty) {
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE products SET quantity = quantity - ? WHERE id=? AND quantity >= ?")) {
            ps.setInt(1, qty);
            ps.setInt(2, id);
            ps.setInt(3, qty);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    public static void saveSale(String station, String customer, double timeCost, double canteenCost, double total) {
        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO sales_history (station_name, customer_name, time_cost, canteen_cost, total_amount) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, station);
            ps.setString(2, customer);
            ps.setDouble(3, timeCost);
            ps.setDouble(4, canteenCost);
            ps.setDouble(5, total);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static List<Object[]> getAllSales() {
        List<Object[]> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM sales_history ORDER BY id DESC")) {
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("id"), rs.getString("station_name"), rs.getString("customer_name"),
                        String.format("%.2f", rs.getDouble("time_cost")),
                        String.format("%.2f", rs.getDouble("canteen_cost")),
                        String.format("%.2f", rs.getDouble("total_amount")),
                        rs.getString("created_at")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public static double getTotalRevenue() {
        try (Connection c = DatabaseManager.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT COALESCE(SUM(total_amount), 0) FROM sales_history")) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }
}