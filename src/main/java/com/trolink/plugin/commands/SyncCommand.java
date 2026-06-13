package com.trolink.plugin.commands;

import com.trolink.plugin.TroLink;
import com.trolink.plugin.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SyncCommand {
    private final TroLink plugin;

    public SyncCommand(TroLink plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("trolink.admin")) {
            MessageUtil.sendMessage(sender, "errors.no-permission");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage("§cKullanim: /trolink sync <oyuncu>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cOyuncu bulunamadi veya cevrimdisi.");
            return;
        }

        plugin.getRoleSyncManager().syncPlayer(target);
        MessageUtil.sendMessage(sender, "admin.sync-started", "%player%", target.getName());
    }
}
