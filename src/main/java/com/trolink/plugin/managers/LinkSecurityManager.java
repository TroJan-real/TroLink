package com.trolink.plugin.managers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LinkSecurityManager {
    private static final int MAX_ATTEMPTS = 3;
    private final Map<UUID, Integer> attempts = new ConcurrentHashMap<>();

    public boolean isRateLimited(UUID uuid) {
        return attempts.getOrDefault(uuid, 0) >= MAX_ATTEMPTS;
    }

    public void incrementAttempts(UUID uuid) {
        attempts.merge(uuid, 1, Integer::sum);
    }

    public void resetAttempts(UUID uuid) {
        attempts.remove(uuid);
    }
}
