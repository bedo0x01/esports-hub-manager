package com.esportshub.engine;

import java.util.*;

// Shared memory store linking active stations between Stations UI and Canteen POS
public class StationTimerService {
    public static final Map<Integer, ActiveSession> sessions = new HashMap<>();

    // Starts a new session on a station
    public static void startSession(int id, String cust, double rate) { sessions.put(id, new ActiveSession(id, cust, rate)); }

    // Ends session and removes it from active map
    public static ActiveSession endSession(int id) { return sessions.remove(id); }

    // Checks if station is currently occupied
    public static boolean isBusy(int id) { return sessions.containsKey(id); }

    // Retrieves active session details for a station
    public static ActiveSession getSession(int id) { return sessions.get(id); }
}