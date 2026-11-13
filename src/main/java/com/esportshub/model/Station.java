package com.esportshub.model;

public class Station {
    public int id;
    public String name;
    public String status;
    public double hourlyRate;

    public Station(int id, String name, String status, double hourlyRate) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.hourlyRate = hourlyRate;
    }
}