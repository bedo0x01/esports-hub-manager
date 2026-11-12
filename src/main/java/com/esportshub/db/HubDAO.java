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
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Station> getAllStations() {
        List<Station> stations = new ArrayList<>();
        String sql = "SELECT * FROM stations";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stations.add(new Station(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("status"),
                        rs.getDouble("hourly_rate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stations;
    }

    public static void updateStationStatus(int stationId, String status) {
        String sql = "UPDATE stations SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, stationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static boolean deductProductStock(int productId, int qtySold) {
        String sql = "UPDATE products SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, qtySold);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, qtySold);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void saveSaleRecord(String stationName, String customerName, double timeCost, double canteenCost, double totalAmount) {
        String sql = "INSERT INTO sales_history (station_name, customer_name, time_cost, canteen_cost, total_amount) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, stationName);
            pstmt.setString(2, customerName);
            pstmt.setDouble(3, timeCost);
            pstmt.setDouble(4, canteenCost);
            pstmt.setDouble(5, totalAmount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM sales_history";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // Fetch all sales history records for the reports table
    public static List<Object[]> getAllSalesRecords() {
        List<Object[]> records = new ArrayList<>();
        String sql = "SELECT id, station_name, customer_name, time_cost, canteen_cost, total_amount, created_at FROM sales_history ORDER BY id DESC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
}