package com.esportshub.model;

public class Station {
    private int id;
    private String name;
    private String status; // "AVAILABLE" or "BUSY"
    private double hourlyRate;

    public Station(int id, String name, String status, double hourlyRate) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.hourlyRate = hourlyRate;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getHourlyRate() { return hourlyRate; }
}