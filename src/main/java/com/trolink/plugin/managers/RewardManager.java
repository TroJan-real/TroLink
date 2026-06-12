package com.trolink.plugin.managers;

import com.trolink.plugin.TroLink;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;

public class RewardManager {
    private final TroLink plugin;

    public RewardManager(TroLink plugin) {
        this.plugin = plugin;
    }

    public void giveRewards(UUID uuid) {
        List<String> commands = plugin.getConfig().getStringList("rewards");
        if (commands.isEmpty()) return;

        String playerName = Bukkit.getOfflinePlayer(uuid).getName();
        if (playerName == null) return;

        Bukkit.getScheduler().runTask(plugin, () -> {
            for (String cmd : commands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", playerName));
            }
        });
    }
}
