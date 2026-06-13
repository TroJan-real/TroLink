package com.trolink.plugin.database;

import com.trolink.plugin.TroLink;
import com.trolink.plugin.database.migration.DatabaseMigration;
import com.trolink.plugin.database.repository.CodeRepository;
import com.trolink.plugin.database.repository.LinkRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {
    private final TroLink plugin;
    private HikariDataSource dataSource;
    private LinkRepository linkRepository;
    private CodeRepository codeRepository;

    public DatabaseManager(TroLink plugin) {
        this.plugin = plugin;
        setupPool();
        this.linkRepository = new LinkRepository(this, plugin);
        this.codeRepository = new CodeRepository(this, plugin);
        new DatabaseMigration(this, plugin).runMigrations();
    }

    private void setupPool() {
        HikariConfig config = new HikariConfig();
        String path = plugin.getDataFolder().getAbsolutePath() + "/database.db";
        config.setJdbcUrl("jdbc:sqlite:" + path);
        config.setDriverClassName("org.sqlite.JDBC");
        config.setPoolName("TroLink-Pool");
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        this.dataSource = new HikariDataSource(config);
    }

    public CompletableFuture<String>  getDiscordId(UUID uuid)                    { return linkRepository.getDiscordId(uuid); }
    public CompletableFuture<UUID>    getUUIDFromDiscord(String discordId)        { return linkRepository.getUUIDByDiscord(discordId); }
    public CompletableFuture<Boolean> linkAccount(UUID uuid, String discordId)    { return linkRepository.insertLink(uuid, discordId); }
    public CompletableFuture<Boolean> unlinkAccount(UUID uuid)                    { return linkRepository.deleteLink(uuid); }
    public CompletableFuture<Boolean> hasReceivedReward(UUID uuid)                { return linkRepository.hasReceivedReward(uuid); }
    public CompletableFuture<Void>    markAsRewarded(UUID uuid)                   { return linkRepository.markAsRewarded(uuid); }

    public Connection getConnection() throws SQLException { return dataSource.getConnection(); }
    public void close()                                   { if (dataSource != null) dataSource.close(); }
    public LinkRepository getLinkRepository()             { return linkRepository; }
    public CodeRepository getCodeRepository()             { return codeRepository; }
}
