package com.esportshub.engine;

import java.time.Duration;
import java.time.LocalDateTime;

public class ActiveSession {
    private final int stationId;
    private final String customerName;
    private final LocalDateTime startTime;
    private final double hourlyRate;
    private double canteenTotal = 0.0;
    private boolean isFinished = false;

    public ActiveSession(int stationId, String customerName, double hourlyRate) {
        this.stationId = stationId;
        this.customerName = customerName;
        this.hourlyRate = hourlyRate;
        this.startTime = LocalDateTime.now();
    }

    public void addCanteenCharge(double amount) {
        this.canteenTotal += amount;
    }

    // حساب الثواني المنقضية بدقة
    public long getElapsedSeconds() {
        return Duration.between(startTime, LocalDateTime.now()).getSeconds();
    }

    // حساب تكلفة الوقت (الدقيقة = السعر بالساعة / 60)
    public double getTimeCost() {
        long seconds = getElapsedSeconds();
        double minutes = seconds / 60.0;
        return minutes * (hourlyRate / 60.0);
    }

    // الإجمالي = تكلفة الوقت + طلبات الكانتين
    public double getTotalAmount() {
        return getTimeCost() + canteenTotal;
    }

    // فورمات الوقت كـ HH:MM:SS عشان يظهر ع الشاشة
    public String getFormattedTime() {
        long sec = getElapsedSeconds();
        long hours = sec / 3600;
        long minutes = (sec % 3600) / 60;
        long remainingSec = sec % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSec);
    }

    public int getStationId() { return stationId; }
    public String getCustomerName() { return customerName; }
    public LocalDateTime getStartTime() { return startTime; }
    public double getCanteenTotal() { return canteenTotal; }
    public boolean isFinished() { return isFinished; }
    public void markFinished() { this.isFinished = true; }
}