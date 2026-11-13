package com.esportshub.engine;

import java.time.Duration;
import java.time.LocalDateTime;

public class ActiveSession {
    public int stationId;
    public String customerName;
    public LocalDateTime startTime;
    public double hourlyRate;
    public double canteenTotal = 0;

    public ActiveSession(int stationId, String customerName, double hourlyRate) {
        this.stationId = stationId;
        this.customerName = customerName;
        this.hourlyRate = hourlyRate;
        this.startTime = LocalDateTime.now();
    }

    public long getSeconds() {
        return Duration.between(startTime, LocalDateTime.now()).getSeconds();
    }

    public double getTimeCost() {
        return (getSeconds() / 3600.0) * hourlyRate;
    }

    public double getTotal() {
        return getTimeCost() + canteenTotal;
    }

    public String getFormattedTime() {
        long s = getSeconds();
        return String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60);
    }
}