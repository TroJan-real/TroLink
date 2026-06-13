package com.trolink.plugin.managers;

import com.trolink.plugin.TroLink;
import net.dv8tion.jda.api.entities.Member;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RoleSyncManager {
    private final TroLink plugin;

    public RoleSyncManager(TroLink plugin) {
        this.plugin = plugin;
    }

    public void syncMember(Member member) {
        if (member == null) return;
        plugin.getDatabaseManager().getUUIDFromDiscord(member.getId()).thenAccept(uuid -> {
            if (uuid == null) return;
            updateLuckPerms(uuid, member);
        });
    }

    public void syncPlayer(Player player) {
        plugin.getDatabaseManager().getDiscordId(player.getUniqueId()).thenAccept(discordId -> {
            if (discordId == null) return;
            var guild = plugin.getDiscordManager().getGuild();
            if (guild == null) return;
            guild.retrieveMemberById(discordId).queue(
                    member -> updateLuckPerms(player.getUniqueId(), member),
                    error -> plugin.getLogger().warning(player.getName() + " icin Discord uyesi bulunamadi.")
            );
        });
    }

    public void removeRoles(String discordId) {
        plugin.getDatabaseManager().getUUIDFromDiscord(discordId).thenAccept(uuid -> {
            if (uuid == null) return;
            LuckPerms lp = LuckPermsProvider.get();
            lp.getUserManager().modifyUser(uuid, user -> {
                var syncSection = plugin.getConfig().getConfigurationSection("role-sync");
                if (syncSection == null) return;
                for (String roleId : syncSection.getKeys(false)) {
                    String group = syncSection.getString(roleId + ".group");
                    if (group != null) {
                        user.data().remove(InheritanceNode.builder(group).build());
                    }
                }
            });
        });
    }

    private void updateLuckPerms(UUID uuid, Member member) {
        LuckPerms lp = LuckPermsProvider.get();
        lp.getUserManager().modifyUser(uuid, user -> {
            var syncSection = plugin.getConfig().getConfigurationSection("role-sync");
            if (syncSection == null) return;
            for (String roleId : syncSection.getKeys(false)) {
                String group = syncSection.getString(roleId + ".group");
                if (group == null) continue;
                InheritanceNode node = InheritanceNode.builder(group).build();
                boolean hasRole = member.getRoles().stream().anyMatch(r -> r.getId().equals(roleId));
                if (hasRole) {
                    user.data().add(node);
                } else {
                    user.data().remove(node);
                }
            }
        });
    }
}
