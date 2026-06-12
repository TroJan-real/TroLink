package com.trolink.plugin.utils;

import com.trolink.plugin.TroLink;
import org.bukkit.command.CommandSender;

public class MessageUtil {

    private MessageUtil() {}

    public static void sendMessage(CommandSender sender, String path, String... placeholders) {
        TroLink plugin = TroLink.getInstance();
        String message = plugin.getConfigManager().getMessage(path);
        String prefix  = plugin.getConfigManager().getMessage("prefix");

        for (int i = 0; i + 1 < placeholders.length; i += 2) {
            message = message.replace(placeholders[i], placeholders[i + 1]);
        }

        if (message.startsWith("[")) {
            String[] lines = plugin.getConfigManager().getMessage(path).replace("{", "").split("\n");
            var list = plugin.getConfig().getStringList(path);
            if (!list.isEmpty()) {
                for (String line : list) {
                    for (int i = 0; i + 1 < placeholders.length; i += 2) {
                        line = line.replace(placeholders[i], placeholders[i + 1]);
                    }
                    sender.sendMessage(ColorUtil.colorize(prefix + line));
                }
                return;
            }
        }

        sender.sendMessage(ColorUtil.colorize(prefix + message));
    }

    public static void sendRawMessage(CommandSender sender, String message) {
        sender.sendMessage(ColorUtil.colorize(message));
    }
}
