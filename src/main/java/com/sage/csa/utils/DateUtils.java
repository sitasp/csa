package com.sage.csa.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static DateTimeFormatter UI_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getTimeFromInstant(Instant instant) {
        // Convert Instant to LocalDateTime using the system's default timezone
        LocalDateTime dateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return UI_FORMAT.format(dateTime);
    }
}
