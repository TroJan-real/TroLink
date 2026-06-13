package com.trolink.plugin.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;
import java.time.Instant;

public class EmbedManager {

    private EmbedManager() {}

    public static MessageEmbed getVerificationEmbed() {
        return new EmbedBuilder()
                .setTitle("TroLink - Hesap Esleme")
                .setDescription(
                        "Minecraft hesabini Discord hesabina baglamak icin\nasagidaki butona tikla.\n\n" +
                        "**Ozel rol kazan**\n" +
                        "**Oyun ici odul al**\n" +
                        "**Yetki senkronizasyonu**\n" +
                        "**Guvenli dogrulama sistemi**"
                )
                .setColor(Color.decode("#00fbff"))
                .setFooter("TroLink | Gelistirici: TroJan_real")
                .setTimestamp(Instant.now())
                .build();
    }
}
