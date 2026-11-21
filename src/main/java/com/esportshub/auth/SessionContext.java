package com.esportshub.auth;

public class SessionContext {
    public static String user = "";
    public static String role = "";

    public static boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}