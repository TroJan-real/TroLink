package com.trolink.plugin.discord;

import com.trolink.plugin.TroLink;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DiscordBot {
    private final TroLink plugin;
    private JDA jda;
    private final AtomicBoolean starting = new AtomicBoolean(false);

    public DiscordBot(TroLink plugin) {
        this.plugin = plugin;
    }

    public void start() throws InterruptedException {
        if (!starting.compareAndSet(false, true)) return;
        if (jda != null
                && jda.getStatus() != JDA.Status.SHUTDOWN
                && jda.getStatus() != JDA.Status.SHUTTING_DOWN) {
            starting.set(false);
            return;
        }

        String token = plugin.getConfig().getString("discord.token", "");
        if (token.isEmpty()) {
            plugin.getLogger().severe("config.yml icinde discord.token bos! Bot baslatilmadi.");
            starting.set(false);
            return;
        }

        try {
            this.jda = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableCache(CacheFlag.ROLE_TAGS)
                    .addEventListeners(
                            new ButtonListener(plugin),
                            new ModalListener(plugin),
                            new RoleSyncListener(plugin)
                    )
                    .build()
                    .awaitReady();
        } finally {
            starting.set(false);
        }
    }

    public void clearAndSendEmbed() {
        if (jda == null) return;

        String channelId = plugin.getConfig().getString("discord.verify-channel-id", "");
        if (channelId.isEmpty()) return;

        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel == null) {
            plugin.getLogger().warning("Dogrulama kanali bulunamadi. ID: " + channelId);
            return;
        }

        channel.getIterableHistory().takeAsync(100).thenCompose(messages -> {
            if (messages.isEmpty()) return CompletableFuture.completedFuture(null);
            List<CompletableFuture<Void>> futures = channel.purgeMessages(messages);
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        }).thenRun(() ->
            channel.sendMessageEmbeds(EmbedManager.getVerificationEmbed())
                    .setActionRow(Button.primary("trolink_verify", "Hesap Esle"))
                    .queue(
                            s -> plugin.getLogger().info("Dogrulama embed'i gonderildi."),
                            e -> plugin.getLogger().severe("Embed gonderilemedi: " + e.getMessage())
                    )
        ).exceptionally(ex -> {
            plugin.getLogger().severe("clearAndSendEmbed hatasi: " + ex.getMessage());
            return null;
        });
    }

    public void shutdown() {
        if (jda == null) return;
        try {
            jda.shutdown();
            if (!jda.awaitShutdown(10, TimeUnit.SECONDS)) {
                jda.shutdownNow();
                plugin.getLogger().warning("JDA zorla kapatildi (10sn timeout).");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            jda.shutdownNow();
        } finally {
            jda = null;
        }
    }

    public JDA getJda() { return jda; }
}
