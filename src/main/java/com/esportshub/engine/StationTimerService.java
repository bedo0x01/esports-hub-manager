package com.esportshub.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.Timer;

public class StationTimerService {
    private static StationTimerService instance;
    private final Map<Integer, ActiveSession> activeSessions = new ConcurrentHashMap<>();
    private Timer backgroundTicker;

    private StationTimerService() {
        initTicker();
    }

    public static synchronized StationTimerService getInstance() {
        if (instance == null) {
            instance = new StationTimerService();
        }
        return instance;
    }

    private void initTicker() {
        // تايمر في الخلفية يحدث الحسابات كل ثانية دون تجميد الواجهة
        backgroundTicker = new Timer(1000, e -> {
            // شغال للتحديث اللحظي
        });
        backgroundTicker.start();
    }

    // بدء جلسة جديدة
    public boolean startSession(int stationId, String customerName, double hourlyRate) {
        if (activeSessions.containsKey(stationId)) {
            return false;
        }
        activeSessions.put(stationId, new ActiveSession(stationId, customerName, hourlyRate));
        return true;
    }

    // إضافة طلب كانتين لجهاز شغال
    public boolean addOrderToStation(int stationId, double itemPrice) {
        ActiveSession session = activeSessions.get(stationId);
        if (session != null) {
            session.addCanteenCharge(itemPrice);
            return true;
        }
        return false;
    }

    // إنهاء الجلسة واستخراج ملخص الحساب
    public ActiveSession endSession(int stationId) {
        ActiveSession session = activeSessions.remove(stationId);
        if (session != null) {
            session.markFinished();
        }
        return session;
    }

    public ActiveSession getSession(int stationId) {
        return activeSessions.get(stationId);
    }

    public boolean isStationBusy(int stationId) {
        return activeSessions.containsKey(stationId);
    }
}