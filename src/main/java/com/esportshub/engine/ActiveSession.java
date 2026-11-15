package com.esportshub.engine;

import java.time.*;

// Tracks an active gaming session's elapsed time, time cost, and canteen orders
public class ActiveSession {
    public int stationId;
    public String customerName;
    public LocalDateTime startTime = LocalDateTime.now();
    public double hourlyRate, canteenTotal = 0;

    public ActiveSession(int stationId, String customerName, double hourlyRate) {
        this.stationId = stationId;
        this.customerName = customerName;
        this.hourlyRate = hourlyRate;
    }

    // Calculates exact elapsed seconds since session started
    public long getSeconds() { return Duration.between(startTime, LocalDateTime.now()).getSeconds(); }

    // Calculates playing time cost based on hourly rate
    public double getTimeCost() { return (getSeconds() / 3600.0) * hourlyRate; }

    // Returns combined bill (playing time + canteen orders)
    public double getTotal() { return getTimeCost() + canteenTotal; }

    // Formats elapsed seconds as HH:MM:SS
    public String getFormattedTime() { long s = getSeconds(); return String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60); }
}