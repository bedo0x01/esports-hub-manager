package com.esportshub.engine;

public class ActiveSession {
    public String player;
    public long startTime = System.currentTimeMillis();
    public double rate, canteenCost = 0;

    public ActiveSession(String player, double rate) {
        this.player = player;
        this.rate = rate;
    }

    public long getSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    public double getTimeCost() {
        return Math.round(((getSeconds() / 3600.0) * rate) * 100.0) / 100.0;
    }

    public double getTotal() {
        return Math.round((getTimeCost() + canteenCost) * 100.0) / 100.0;
    }
}