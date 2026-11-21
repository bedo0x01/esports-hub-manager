package com.esportshub.model;

public class Product {
    public int id, qty;
    public String name;
    public double price;

    public Product(int id, String name, double price, int qty) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    public String toString() {
        return name + " (" + price + " EGP)";
    }
}