package com.esportshub.engine;

import java.util.*;

public class SessionEngine {
    public static class Session {
        public String player;
        public long start = System.currentTimeMillis();
        public double rate, canteen = 0;

        public Session(String player, double rate) {
            this.player = player;
            this.rate = rate;
        }

        public long seconds() { return (System.currentTimeMillis() - start) / 1000; }
        public double timeCost() { return Math.round(((seconds() / 3600.0) * rate) * 100.0) / 100.0; }
        public double total() { return Math.round((timeCost() + canteen) * 100.0) / 100.0; }
    }

    public static Map<Integer, Session> active = new HashMap<>();

    public static void start(int id, String player, double rate) { active.put(id, new Session(player, rate)); }
    public static Session stop(int id) { return active.remove(id); }
    public static boolean isBusy(int id) { return active.containsKey(id); }
}
