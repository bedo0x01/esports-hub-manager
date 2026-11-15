package com.esportshub.auth;

// Holds logged-in user info and role for RBAC permission checks
public class SessionContext {
    public static String username = "", role = "";

    // Returns true if current user is an ADMIN
    public static boolean isAdmin() { return "ADMIN".equalsIgnoreCase(role); }
}