package com.trolink.plugin.integrations;

import com.trolink.plugin.TroLink;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion {
    private final TroLink plugin;

    public PlaceholderAPIHook(TroLink plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getAuthor()     { return "TroJan_real"; }

    @Override
    public @NotNull String getIdentifier() { return "trolink"; }

    @Override
    public @NotNull String getVersion()    { return plugin.getDescription().getVersion(); }

    @Override
    public boolean persist()               { return true; }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";

        if (params.equalsIgnoreCase("status")) {
            String discordId = plugin.getCacheManager().getCachedDiscordId(player.getUniqueId());
            return discordId != null ? "§aBagli" : "§cBagli Degil";
        }

        if (params.equalsIgnoreCase("discord_id")) {
            String discordId = plugin.getCacheManager().getCachedDiscordId(player.getUniqueId());
            return discordId != null ? discordId : "N/A";
        }

        return null;
    }
}
