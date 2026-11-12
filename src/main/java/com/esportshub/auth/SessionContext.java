package com.esportshub.auth;

public class SessionContext {
    private static String currentUsername;
    private static String currentRole;

    public static void login(String username, String role) {
        currentUsername = username;
        currentRole = role;
    }

    public static void logout() {
        currentUsername = null;
        currentRole = null;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static String getCurrentRole() {
        return currentRole;
    }

    public static boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(currentRole);
    }
}