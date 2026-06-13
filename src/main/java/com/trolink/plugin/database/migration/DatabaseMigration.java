package com.trolink.plugin.database.migration;

import com.trolink.plugin.TroLink;
import com.trolink.plugin.database.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseMigration {
    private final DatabaseManager db;
    private final TroLink plugin;

    public DatabaseMigration(DatabaseManager db, TroLink plugin) {
        this.db = db;
        this.plugin = plugin;
    }

    public void runMigrations() {
        try (Connection conn = db.getConnection(); Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS linked_accounts (
                    uuid TEXT PRIMARY KEY,
                    discord_id TEXT UNIQUE,
                    linked_at BIGINT
                )
            """);
            st.execute("""
                CREATE TABLE IF NOT EXISTS rewarded_players (
                    uuid TEXT PRIMARY KEY,
                    rewarded_at BIGINT
                )
            """);
        } catch (SQLException e) {
            plugin.getLogger().severe("Veritabani migrasyonu basarisiz: " + e.getMessage());
        }
    }
}
