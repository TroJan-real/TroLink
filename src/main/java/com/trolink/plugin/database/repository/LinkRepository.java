package com.trolink.plugin.database.repository;

import com.trolink.plugin.TroLink;
import com.trolink.plugin.database.DatabaseManager;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LinkRepository {
    private final DatabaseManager db;
    private final TroLink plugin;

    public LinkRepository(DatabaseManager db, TroLink plugin) {
        this.db = db;
        this.plugin = plugin;
    }

    public CompletableFuture<Boolean> insertLink(UUID uuid, String discordId) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "INSERT INTO linked_accounts (uuid, discord_id, linked_at) VALUES (?, ?, ?)";
            try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, uuid.toString());
                ps.setString(2, discordId);
                ps.setLong(3, System.currentTimeMillis());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                plugin.getLogger().severe("insertLink hatasi: " + e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<String> getDiscordId(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT discord_id FROM linked_accounts WHERE uuid = ?";
            try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                return rs.next() ? rs.getString("discord_id") : null;
            } catch (SQLException e) {
                plugin.getLogger().severe("getDiscordId hatasi: " + e.getMessage());
                return null;
            }
        });
    }

    public CompletableFuture<UUID> getUUIDByDiscord(String discordId) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT uuid FROM linked_accounts WHERE discord_id = ?";
            try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, discordId);
                ResultSet rs = ps.executeQuery();
                return rs.next() ? UUID.fromString(rs.getString("uuid")) : null;
            } catch (SQLException e) {
                plugin.getLogger().severe("getUUIDByDiscord hatasi: " + e.getMessage());
                return null;
            }
        });
    }

    public CompletableFuture<Boolean> deleteLink(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "DELETE FROM linked_accounts WHERE uuid = ?";
            try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, uuid.toString());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                plugin.getLogger().severe("deleteLink hatasi: " + e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> hasReceivedReward(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT 1 FROM rewarded_players WHERE uuid = ?";
            try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, uuid.toString());
                return ps.executeQuery().next();
            } catch (SQLException e) {
                plugin.getLogger().severe("hasReceivedReward hatasi: " + e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<Void> markAsRewarded(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            String sql = "INSERT OR IGNORE INTO rewarded_players (uuid, rewarded_at) VALUES (?, ?)";
            try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, uuid.toString());
                ps.setLong(2, System.currentTimeMillis());
                ps.executeUpdate();
            } catch (SQLException e) {
                plugin.getLogger().severe("markAsRewarded hatasi: " + e.getMessage());
            }
        });
    }
}
