package com.trolink.plugin.models;

import java.util.UUID;

public record LinkedAccount(
        UUID uuid,
        String discordId,
        long linkedAt
) {}
