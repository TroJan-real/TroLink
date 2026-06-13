package com.trolink.plugin.listeners;

import com.trolink.plugin.TroLink;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final TroLink plugin;

    public PlayerQuitListener(TroLink plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getCacheManager().removeCachedLink(event.getPlayer().getUniqueId());
        plugin.getSecurityManager().resetAttempts(event.getPlayer().getUniqueId());
    }
}
