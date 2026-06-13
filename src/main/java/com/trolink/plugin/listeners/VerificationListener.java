package com.trolink.plugin.listeners;

import com.trolink.plugin.TroLink;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@SuppressWarnings("deprecation")
public class VerificationListener implements Listener {
    private final TroLink plugin;

    public VerificationListener(TroLink plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!plugin.getConfig().getBoolean("link.require-link-for-chat", false)) return;

        String discordId = plugin.getCacheManager().getCachedDiscordId(event.getPlayer().getUniqueId());
        if (discordId == null) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§c§lHATA! §7Sohbet edebilmek icin Discord hesabinizi baglamaniz gerekmektedir.");
            event.getPlayer().sendMessage("§eKomut: §f/hesapesle");
        }
    }
}
