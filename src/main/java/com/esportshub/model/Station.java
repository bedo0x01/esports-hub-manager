package com.esportshub.model;

// Represents a gaming station (PC or PS5) and its hourly rate
public class Station {
    public int id;
    public String name, status;
    public double hourlyRate;

    public Station(int id, String name, String status, double hourlyRate) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.hourlyRate = hourlyRate;
    }
}