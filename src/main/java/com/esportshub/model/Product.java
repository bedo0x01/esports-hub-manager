package com.esportshub.model;

// Represents a canteen item with price and available stock
public class Product {
    public int id, quantity;
    public String name;
    public double price;

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Formats item display inside JComboBox dropdowns
    public String toString() {
        return name + " (" + price + " EGP)";
    }
}