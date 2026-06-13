package com.trolink.plugin.models;

import java.util.UUID;

public record VerificationCode(
        String code,
        UUID uuid,
        long expiry
) {}
