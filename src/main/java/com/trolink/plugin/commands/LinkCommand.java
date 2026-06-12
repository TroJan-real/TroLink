package com.trolink.plugin.commands;

import com.trolink.plugin.TroLink;
import com.trolink.plugin.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LinkCommand implements CommandExecutor {
    private final TroLink plugin;

    public LinkCommand(TroLink plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtil.sendMessage(sender, "errors.only-players");
            return true;
        }

        plugin.getDatabaseManager().getDiscordId(player.getUniqueId()).thenAccept(discordId -> {
            if (discordId != null) {
                MessageUtil.sendMessage(player, "errors.already-linked");
                return;
            }
            String code = plugin.getCodeManager().generateCode(player.getUniqueId());
            MessageUtil.sendMessage(player, "link.code-generated", "%code%", code);
        });

        return true;
    }
}
