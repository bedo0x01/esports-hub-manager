package com.esportshub.engine;

public class EngineTest {
    public static void main(String[] args) throws InterruptedException {
        StationTimerService engine = StationTimerService.getInstance();

        System.out.println("بدء جلسة لجهاز 1 (السعر 30 ج/س)...");
        engine.startSession(1, "Ahmed", 30.0);

        Thread.sleep(2500); // محاكاة مرور ثانيتين ونص

        System.out.println("الوقت: " + engine.getSession(1).getFormattedTime());

        engine.addOrderToStation(1, 15.0); // كانز بـ 15 جنيه
        System.out.printf("الإجمالي الحالي: %.2f EGP%n", engine.getSession(1).getTotalAmount());

        ActiveSession receipt = engine.endSession(1);
        System.out.printf("تم إنهاء الجلسة! الحساب النهائي: %.2f EGP%n", receipt.getTotalAmount());
    }
}