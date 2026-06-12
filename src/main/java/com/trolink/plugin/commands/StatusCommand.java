package com.trolink.plugin.commands;

import com.trolink.plugin.TroLink;
import com.trolink.plugin.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StatusCommand implements CommandExecutor {
    private final TroLink plugin;

    public StatusCommand(TroLink plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtil.sendMessage(sender, "errors.only-players");
            return true;
        }

        plugin.getDatabaseManager().getDiscordId(player.getUniqueId()).thenAccept(discordId -> {
            if (discordId == null) {
                MessageUtil.sendMessage(player, "status.not-linked");
            } else {
                MessageUtil.sendMessage(player, "status.linked", "%discord_id%", discordId);
            }
        });

        return true;
    }
}
