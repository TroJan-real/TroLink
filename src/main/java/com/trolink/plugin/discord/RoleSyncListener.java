package com.trolink.plugin.discord;

import com.trolink.plugin.TroLink;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RoleSyncListener extends ListenerAdapter {
    private final TroLink plugin;

    public RoleSyncListener(TroLink plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        plugin.getRoleSyncManager().syncMember(event.getMember());
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        plugin.getRoleSyncManager().syncMember(event.getMember());
    }
}
