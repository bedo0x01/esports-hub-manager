package com.esportshub.model;

import java.time.LocalDateTime;

public class GameSession {
    private int stationId;
    private String customerName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalAmount;

    public GameSession(int stationId, String customerName) {
        this.stationId = stationId;
        this.customerName = customerName;
        this.startTime = LocalDateTime.now();
    }

    public int getStationId() { return stationId; }
    public String getCustomerName() { return customerName; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
}