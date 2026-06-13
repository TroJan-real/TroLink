package com.trolink.plugin.database.repository;

import com.trolink.plugin.TroLink;
import com.trolink.plugin.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CodeRepository {
    private final DatabaseManager db;
    private final TroLink plugin;

    public CodeRepository(DatabaseManager db, TroLink plugin) {
        this.db = db;
        this.plugin = plugin;
    }

    public void cleanExpiredCodes() {
        String sql = "DELETE FROM verify_codes WHERE expire < ?";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, System.currentTimeMillis());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().warning("cleanExpiredCodes hatasi: " + e.getMessage());
        }
    }
}
