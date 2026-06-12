package com.trolink.plugin.managers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {
    private final Map<UUID, String> cache = new ConcurrentHashMap<>();

    public void cacheLink(UUID uuid, String discordId)  { cache.put(uuid, discordId); }
    public String getCachedDiscordId(UUID uuid)         { return cache.get(uuid); }
    public void removeCachedLink(UUID uuid)             { cache.remove(uuid); }
}
