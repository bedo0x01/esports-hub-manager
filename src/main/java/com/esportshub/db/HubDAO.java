package com.esportshub.db;

import com.esportshub.model.*;
import java.sql.*;
import java.util.*;

public class HubDAO {

    public static String login(String user, String pass) {
        try (Connection c = DatabaseManager.connect();
             PreparedStatement ps = c.prepareStatement("SELECT role FROM users WHERE username=? AND password=?")) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("role");
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public static List<Station> getStations() {
        List<Station> list = new ArrayList<>();
        try (Connection c = DatabaseManager.connect();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM stations")) {
            while (rs.next()) list.add(new Station(rs.getInt("id"), rs.getString("name"), rs.getDouble("rate")));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public static List<Product> getProducts() {
        List<Product> list = new ArrayList<>();
        try (Connection c = DatabaseManager.connect();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM products")) {
            while (rs.next()) list.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("qty")));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public static boolean buyProduct(int id, int count) {
        try (Connection c = DatabaseManager.connect();
             PreparedStatement ps = c.prepareStatement("UPDATE products SET qty = qty - ? WHERE id=? AND qty >= ?")) {
            ps.setInt(1, count); ps.setInt(2, id); ps.setInt(3, count);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { return false; }
    }

    public static void addSale(String st, String cust, double timeCost, double canteenCost) {
        try (Connection c = DatabaseManager.connect();
             PreparedStatement ps = c.prepareStatement("INSERT INTO sales (station,customer,time_cost,canteen_cost,total) VALUES (?,?,?,?,?)")) {
            ps.setString(1, st); ps.setString(2, cust);
            ps.setDouble(3, timeCost); ps.setDouble(4, canteenCost);
            ps.setDouble(5, timeCost + canteenCost);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static List<Object[]> getSales() {
        List<Object[]> list = new ArrayList<>();
        try (Connection c = DatabaseManager.connect();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM sales ORDER BY id DESC")) {
            while (rs.next()) {
                list.add(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6)});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public static double getTotalRevenue() {
        try (Connection c = DatabaseManager.connect();
             ResultSet rs = c.createStatement().executeQuery("SELECT SUM(total) FROM sales")) {
            return rs.getDouble(1);
        } catch (Exception e) { return 0; }
    }

    public static List<Object[]> getUsers() {
        List<Object[]> list = new ArrayList<>();
        try (Connection c = DatabaseManager.connect();
             ResultSet rs = c.createStatement().executeQuery("SELECT id, username, role FROM users")) {
            while (rs.next()) list.add(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3)});
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public static void addUser(String u, String p, String role) {
        try (Connection c = DatabaseManager.connect();
             PreparedStatement ps = c.prepareStatement("INSERT INTO users (username,password,role) VALUES (?,?,?)")) {
            ps.setString(1, u); ps.setString(2, p); ps.setString(3, role);
            ps.executeUpdate();
        } catch (Exception ignored) {}
    }

    public static void deleteUser(int id) {
        if (id == 1) return; // حماية الأدمن الرئيسي
        try (Connection c = DatabaseManager.connect();
             PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception ignored) {}
    }
}