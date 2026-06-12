package com.trolink.plugin.discord;

import com.trolink.plugin.TroLink;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.bukkit.Bukkit;

import java.util.UUID;

public class NicknameManager {
    private final TroLink plugin;

    public NicknameManager(TroLink plugin) {
        this.plugin = plugin;
    }

    public void updateNickname(Member member, UUID uuid) {
        if (member == null) return;
        if (!member.getGuild().getSelfMember().canInteract(member)) {
            plugin.getLogger().warning("Takma ad degistirilemedi (hiyerarsi): "
                    + member.getUser().getName() + " — Bot rolunu Discord'da ust siraya tasiyin.");
            return;
        }

        String mcName = Bukkit.getOfflinePlayer(uuid).getName();
        if (mcName == null) return;

        String format = plugin.getConfig().getString("nickname-format", "%minecraft% | %discord%");
        String raw = format
                .replace("%minecraft%", mcName)
                .replace("%discord%", member.getUser().getEffectiveName());

        String newName = raw.length() > 32 ? raw.substring(0, 32) : raw;

        member.modifyNickname(newName).queue(
                s -> plugin.getLogger().info("Takma ad guncellendi: " + member.getUser().getName() + " -> " + newName),
                e -> {
                    if (e instanceof HierarchyException) {
                        plugin.getLogger().warning("Takma ad degistirilemedi (hiyerarsi): " + member.getUser().getName());
                    } else if (e instanceof InsufficientPermissionException) {
                        plugin.getLogger().warning("Takma ad degistirilemedi (yetki yok): " + member.getUser().getName());
                    } else {
                        plugin.getLogger().warning("Takma ad degistirilemedi: " + e.getMessage());
                    }
                }
        );
    }
}
