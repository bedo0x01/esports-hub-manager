package com.esportshub.core;

import com.esportshub.model.Product;
import com.esportshub.model.Station;

import java.util.ArrayList;
import java.util.List;

public class MockData {
    public static List<Station> getStations() {
        List<Station> list = new ArrayList<>();
        list.add(new Station(1, "PC 1", "AVAILABLE", 25.0));
        list.add(new Station(2, "PC 2", "AVAILABLE", 25.0));
        list.add(new Station(3, "PC 3", "AVAILABLE", 25.0));
        list.add(new Station(4, "PC 4", "AVAILABLE", 25.0));
        list.add(new Station(5, "PS5 1", "AVAILABLE", 40.0));
        list.add(new Station(6, "PS5 2", "AVAILABLE", 40.0));
        return list;
    }

    public static List<Product> getProducts() {
        List<Product> list = new ArrayList<>();
        list.add(new Product(1, "Pepsi", 15.0, 20));
        list.add(new Product(2, "Red Bull", 45.0, 10));
        list.add(new Product(3, "Chips", 10.0, 15));
        list.add(new Product(4, "Water", 7.0, 30));
        return list;
    }
}