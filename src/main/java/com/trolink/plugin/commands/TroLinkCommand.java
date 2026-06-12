package com.trolink.plugin.commands;

import com.trolink.plugin.TroLink;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TroLinkCommand implements CommandExecutor, TabCompleter {
    private final TroLink plugin;
    private final ReloadCommand reload;
    private final SyncCommand sync;

    public TroLinkCommand(TroLink plugin) {
        this.plugin = plugin;
        this.reload = new ReloadCommand(plugin);
        this.sync = new SyncCommand(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("trolink.admin")) {
            sender.sendMessage("§cBu islemi yapmak icin yetkiniz yok!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§b§lTroLink §7| Kullanim: §f/trolink <reload|sync|embed>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> reload.execute(sender);
            case "sync"   -> sync.execute(sender, args);
            case "embed"  -> {
                sender.sendMessage("§7Dogrulama embed'i yeniden gonderiliyor...");
                plugin.getDiscordBot().clearAndSendEmbed();
                sender.sendMessage("§aEmbed gonderme islemi baslatildi.");
            }
            default -> sender.sendMessage("§cBilinmeyen komut. Kullanim: §f/trolink <reload|sync|embed>");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return List.of("reload", "sync", "embed");
        if (args.length == 2 && args[0].equalsIgnoreCase("sync")) return List.of("<oyuncu>");
        return List.of();
    }
}
