package com.esportshub.engine;

import java.util.HashMap;
import java.util.Map;

public class StationTimerService {
    public static final Map<Integer, ActiveSession> sessions = new HashMap<>();

    public static void startSession(int stationId, String customer, double rate) {
        sessions.put(stationId, new ActiveSession(stationId, customer, rate));
    }

    public static ActiveSession endSession(int stationId) {
        return sessions.remove(stationId);
    }

    public static boolean isBusy(int stationId) {
        return sessions.containsKey(stationId);
    }

    public static ActiveSession getSession(int stationId) {
        return sessions.get(stationId);
    }
}