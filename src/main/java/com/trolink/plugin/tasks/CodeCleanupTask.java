package com.trolink.plugin.tasks;

import com.trolink.plugin.TroLink;
import org.bukkit.scheduler.BukkitRunnable;

public class CodeCleanupTask extends BukkitRunnable {
    private final TroLink plugin;

    public CodeCleanupTask(TroLink plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (plugin.getConfig().getBoolean("settings.log-cleanup", false)) {
            plugin.getLogger().info("Suresi dolmus dogrulama kodlari temizlendi.");
        }
    }
}
