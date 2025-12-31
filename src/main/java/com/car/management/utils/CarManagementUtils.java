package com.car.management.utils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public class CarManagementUtils {

    public static String getSessionUser() {
        return "Test user";
    }
    public static LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
