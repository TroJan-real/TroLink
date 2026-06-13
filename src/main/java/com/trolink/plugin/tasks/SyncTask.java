package com.trolink.plugin.tasks;

import com.trolink.plugin.TroLink;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class SyncTask extends BukkitRunnable {
    private final TroLink plugin;

    public SyncTask(TroLink plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().isEmpty()) return;
        for (var player : Bukkit.getOnlinePlayers()) {
            plugin.getRoleSyncManager().syncPlayer(player);
        }
    }
}
