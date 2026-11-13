package com.esportshub.auth;

public class SessionContext {
    public static String username = "";
    public static String role = "";

    public static boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
}