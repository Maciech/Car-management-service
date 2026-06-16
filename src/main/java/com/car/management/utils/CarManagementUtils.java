package com.car.management.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class CarManagementUtils {

    public static String getSessionUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return "anonymous";
        return auth.getName();
    }
    public static LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
