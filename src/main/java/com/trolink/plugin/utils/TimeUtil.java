package com.trolink.plugin.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtil {

    private TimeUtil() {}

    public static String formatTime(long millis) {
        if (millis <= 0) return "0s";
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
        StringBuilder sb = new StringBuilder();
        if (minutes > 0) sb.append(minutes).append("m ");
        if (seconds > 0) sb.append(seconds).append("s");
        return sb.toString().trim();
    }
}
