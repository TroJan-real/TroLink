package com.trolink.plugin.managers;

import com.trolink.plugin.models.VerificationCode;

import java.security.SecureRandom;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CodeManager {
    private final Map<String, VerificationCode> activeCodes = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public String generateCode(UUID uuid) {
        activeCodes.values().removeIf(v -> v.uuid().equals(uuid));
        activeCodes.entrySet().removeIf(e -> System.currentTimeMillis() > e.getValue().expiry());

        String code;
        do {
            code = String.format("%06d", random.nextInt(1_000_000));
        } while (activeCodes.containsKey(code));

        long expireAt = System.currentTimeMillis() + 300_000L;
        activeCodes.put(code, new VerificationCode(code, uuid, expireAt));
        return code;
    }

    public VerificationCode validateCode(String code) {
        VerificationCode vc = activeCodes.get(code);
        if (vc == null) return null;
        if (System.currentTimeMillis() > vc.expiry()) {
            activeCodes.remove(code);
            return null;
        }
        return vc;
    }

    public void invalidateCode(String code) {
        activeCodes.remove(code);
    }
}
