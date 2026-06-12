package com.trolink.plugin.listeners;

import com.trolink.plugin.TroLink;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final TroLink plugin;

    public PlayerJoinListener(TroLink plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getDatabaseManager().getLinkRepository().getDiscordId(player.getUniqueId())
                .thenAccept(discordId -> {
                    if (discordId == null) return;
                    plugin.getCacheManager().cacheLink(player.getUniqueId(), discordId);
                    plugin.getRoleSyncManager().syncPlayer(player);
                });
    }
}
