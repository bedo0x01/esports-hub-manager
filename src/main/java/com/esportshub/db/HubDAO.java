package com.esportshub.db;

import com.esportshub.model.Product;
import com.esportshub.model.Station;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HubDAO {

    public static String authenticateUser(String username, String password) {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static List<Object[]> getAllUsers() {
        List<Object[]> users = new ArrayList<>();
        String sql = "SELECT id, username, role FROM users ORDER BY id ASC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("role")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    public static boolean addUser(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            return false;
        }
    }

    public static boolean updateUserPassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean deleteUser(int userId) {
        if (userId == 1) return false; // Protect primary admin
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static List<Station> getAllStations() {
        List<Station> list = new ArrayList<>();
        String sql = "SELECT * FROM stations";
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Station(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("status"),
                        rs.getDouble("hourly_rate")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static void updateStationStatus(int stationId, String status) {
        String sql = "UPDATE stations SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, stationId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static boolean deductProductStock(int productId, int qtySold) {
        String sql = "UPDATE products SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qtySold);
            ps.setInt(2, productId);
            ps.setInt(3, qtySold);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void saveSaleRecord(String stationName, String customerName, double timeCost, double canteenCost, double totalAmount) {
        String sql = "INSERT INTO sales_history (station_name, customer_name, time_cost, canteen_cost, total_amount) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, stationName);
            ps.setString(2, customerName);
            ps.setDouble(3, timeCost);
            ps.setDouble(4, canteenCost);
            ps.setDouble(5, totalAmount);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Object[]> getAllSalesRecords() {
        List<Object[]> records = new ArrayList<>();
        String sql = "SELECT id, station_name, customer_name, time_cost, canteen_cost, total_amount, created_at FROM sales_history ORDER BY id DESC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                records.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("station_name"),
                        rs.getString("customer_name"),
                        String.format("%.2f", rs.getDouble("time_cost")),
                        String.format("%.2f", rs.getDouble("canteen_cost")),
                        String.format("%.2f", rs.getDouble("total_amount")),
                        rs.getString("created_at")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return records;
    }

    public static double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM sales_history";
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0.0;
    }
}