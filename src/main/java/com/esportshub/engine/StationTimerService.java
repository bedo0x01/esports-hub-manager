package com.esportshub.engine;

import java.util.*;

public class StationTimerService {
    public static Map<Integer, ActiveSession> active = new HashMap<>();

    public static void start(int id, String player, double rate) {
        active.put(id, new ActiveSession(player, rate));
    }

    public static ActiveSession stop(int id) {
        return active.remove(id);
    }

    public static boolean isBusy(int id) {
        return active.containsKey(id);
    }
}