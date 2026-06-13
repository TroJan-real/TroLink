package com.trolink.plugin.discord;

import com.trolink.plugin.TroLink;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class ButtonListener extends ListenerAdapter {
    private final TroLink plugin;

    public ButtonListener(TroLink plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (!event.getComponentId().equals("trolink_verify")) return;

        TextInput codeInput = TextInput.create("verify_code", "Dogrulama Kodu", TextInputStyle.SHORT)
                .setPlaceholder("Oyun icinden aldiginiz 6 haneli kod")
                .setMinLength(6)
                .setMaxLength(6)
                .setRequired(true)
                .build();

        Modal modal = Modal.create("trolink_modal", "TroLink Dogrulama")
                .addActionRow(codeInput)
                .build();

        event.replyModal(modal).queue();
    }
}
