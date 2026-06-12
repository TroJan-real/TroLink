package com.trolink.plugin.discord;

import com.trolink.plugin.TroLink;
import com.trolink.plugin.models.VerificationCode;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ModalListener extends ListenerAdapter {
    private final TroLink plugin;

    public ModalListener(TroLink plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.getModalId().equals("trolink_modal")) return;

        String codeInput = event.getValue("verify_code").getAsString().trim();
        VerificationCode vc = plugin.getCodeManager().validateCode(codeInput);

        if (vc == null) {
            event.reply("Kod hatali veya suresi dolmus. Lutfen oyundan tekrar kod alin.")
                    .setEphemeral(true).queue();
            return;
        }

        event.deferReply(true).queue();

        plugin.getDatabaseManager().linkAccount(vc.uuid(), event.getUser().getId()).thenAccept(success -> {
            if (success) {
                plugin.getCodeManager().invalidateCode(codeInput);
                giveVerifiedRole(event);
                plugin.getNicknameManager().updateNickname(event.getMember(), vc.uuid());
                plugin.getRoleSyncManager().syncMember(event.getMember());

                plugin.getDatabaseManager().hasReceivedReward(vc.uuid()).thenAccept(alreadyRewarded -> {
                    if (!alreadyRewarded) {
                        plugin.getRewardManager().giveRewards(vc.uuid());
                        plugin.getDatabaseManager().markAsRewarded(vc.uuid());
                        plugin.getLogger().info("Odul verildi: " + event.getUser().getName());
                    }
                });

                event.getHook().sendMessage("Hesabiniz basariyla baglandi!")
                        .setEphemeral(true).queue();
            } else {
                event.getHook().sendMessage("Bu Discord veya Minecraft hesabi zaten baska bir hesaba bagli.")
                        .setEphemeral(true).queue();
            }
        }).exceptionally(ex -> {
            plugin.getLogger().severe("Hesap baglama hatasi: " + ex.getMessage());
            event.getHook().sendMessage("Sunucu hatasi olustu, lutfen tekrar deneyin.")
                    .setEphemeral(true).queue();
            return null;
        });
    }

    private void giveVerifiedRole(ModalInteractionEvent event) {
        String roleId = plugin.getConfig().getString("discord.verified-role-id", "");
        if (roleId.isEmpty()) return;

        Guild guild = event.getGuild();
        if (guild == null) return;

        Role role = guild.getRoleById(roleId);
        if (role == null) {
            plugin.getLogger().warning("Verified rol bulunamadi. ID: " + roleId);
            return;
        }

        guild.addRoleToMember(event.getMember(), role).queue(
                v -> plugin.getLogger().info("Verified rol verildi: " + event.getUser().getName()),
                e -> plugin.getLogger().severe("Rol verilemedi (" + event.getUser().getName() + "): " + e.getMessage())
        );
    }
}
