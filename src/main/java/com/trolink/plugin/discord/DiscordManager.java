package com.trolink.plugin.discord;

import com.trolink.plugin.TroLink;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class DiscordManager {
    private final TroLink plugin;

    public DiscordManager(TroLink plugin) {
        this.plugin = plugin;
    }

    public Guild getGuild() {
        JDA jda = plugin.getDiscordBot().getJda();
        if (jda == null) return null;
        return jda.getGuildById(plugin.getConfig().getString("discord.guild-id", ""));
    }
}
