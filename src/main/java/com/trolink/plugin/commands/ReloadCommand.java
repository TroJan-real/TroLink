package com.trolink.plugin.commands;

import com.trolink.plugin.TroLink;
import com.trolink.plugin.utils.MessageUtil;
import org.bukkit.command.CommandSender;

public class ReloadCommand {
    private final TroLink plugin;

    public ReloadCommand(TroLink plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender) {
        if (!sender.hasPermission("trolink.admin")) {
            MessageUtil.sendMessage(sender, "errors.no-permission");
            return;
        }
        plugin.getConfigManager().reloadConfigs();
        MessageUtil.sendMessage(sender, "admin.reloaded");
    }
}
