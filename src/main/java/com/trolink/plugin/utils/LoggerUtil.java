package com.trolink.plugin.utils;

import com.trolink.plugin.TroLink;

import java.util.logging.Level;

public class LoggerUtil {

    private LoggerUtil() {}

    public static void info(String message)  { TroLink.getInstance().getLogger().info(message); }
    public static void warn(String message)  { TroLink.getInstance().getLogger().warning(message); }
    public static void error(String message) { TroLink.getInstance().getLogger().severe(message); }

    public static void debug(String message) {
        if (TroLink.getInstance().getConfig().getBoolean("debug", false)) {
            TroLink.getInstance().getLogger().log(Level.INFO, "[DEBUG] " + message);
        }
    }
}
