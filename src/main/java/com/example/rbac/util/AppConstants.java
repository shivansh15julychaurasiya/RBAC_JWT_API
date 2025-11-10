package com.example.rbac.util;

public final class AppConstants {
    private AppConstants() {}

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CLAIM_ROLES = "roles";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    public static final String MSG_LOGIN_SUCCESS = "Login successful";
    public static final String MSG_LOGIN_FAILED = "Invalid username or password";
    public static final String MSG_UNAUTHORIZED = "You are not authorized to access this resource";

    public static final String[] PUBLIC_ENDPOINTS = {
        "/api/auth/**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-ui.html"
    };
}
