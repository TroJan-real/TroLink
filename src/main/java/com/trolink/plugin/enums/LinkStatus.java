package com.trolink.plugin.enums;

public enum LinkStatus {

    LINKED("§aBağlı"),
    PENDING("§eBeklemede"),
    UNLINKED("§cBağlı Değil");

    private final String displayName;

    LinkStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
