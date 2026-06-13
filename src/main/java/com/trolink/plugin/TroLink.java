package com.trolink.plugin;

import com.trolink.plugin.commands.*;
import com.trolink.plugin.database.DatabaseManager;
import com.trolink.plugin.discord.*;
import com.trolink.plugin.integrations.*;
import com.trolink.plugin.listeners.*;
import com.trolink.plugin.managers.*;
import com.trolink.plugin.metrics.MetricsManager;
import com.trolink.plugin.tasks.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TroLink extends JavaPlugin {

    private static TroLink instance;

    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private CodeManager codeManager;
    private RoleSyncManager roleSyncManager;
    private RewardManager rewardManager;
    private CacheManager cacheManager;
    private LinkSecurityManager securityManager;

    private DiscordBot discordBot;
    private DiscordManager discordManager;
    private NicknameManager nicknameManager;

    private LuckPermsHook luckPermsHook;
    private PlaceholderAPIHook placeholderAPIHook;
    private MetricsManager metricsManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        this.databaseManager = new DatabaseManager(this);
        this.codeManager = new CodeManager();
        this.cacheManager = new CacheManager();
        this.securityManager = new LinkSecurityManager();
        this.rewardManager = new RewardManager(this);
        this.roleSyncManager = new RoleSyncManager(this);
        this.discordManager = new DiscordManager(this);
        this.nicknameManager = new NicknameManager(this);
        this.discordBot = new DiscordBot(this);

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                this.discordBot.start();
                getLogger().info("Discord botu başarıyla bağlandı.");
                this.discordBot.clearAndSendEmbed();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                getLogger().warning("Bot başlatma işlemi kesildi.");
            } catch (Exception e) {
                getLogger().severe("Discord botu başlatılamadı: " + e.getMessage());
            }
        });

        this.luckPermsHook = new LuckPermsHook();

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.placeholderAPIHook = new PlaceholderAPIHook(this);
            this.placeholderAPIHook.register();
            getLogger().info("PlaceholderAPI entegrasyonu aktif.");
        }

        registerCommands();
        registerListeners();
        startTasks();

        // bStats entegrasyonu - istatistik takibi
        this.metricsManager = new MetricsManager(this);

        getLogger().info("TroLink v" + getDescription().getVersion() + " aktif. Gelistirici: TroJan_real");
    }

    @Override
    public void onDisable() {
        if (discordBot != null) {
            discordBot.shutdown();
        }
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("TroLink devre disi birakildi.");
    }

    private void registerCommands() {
        getCommand("hesapesle").setExecutor(new LinkCommand(this));
        getCommand("hesapkaldir").setExecutor(new UnlinkCommand(this));
        getCommand("hesapdurum").setExecutor(new StatusCommand(this));

        TroLinkCommand adminCommand = new TroLinkCommand(this);
        getCommand("trolink").setExecutor(adminCommand);
        getCommand("trolink").setTabCompleter(adminCommand);
    }

    private void registerListeners() {
        var pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(this), this);
        pm.registerEvents(new PlayerQuitListener(this), this);
        pm.registerEvents(new VerificationListener(this), this);
    }

    private void startTasks() {
        new CodeCleanupTask(this).runTaskTimerAsynchronously(this, 1200L, 1200L);
        new SyncTask(this).runTaskTimerAsynchronously(this, 36000L, 36000L);
    }

    public static TroLink getInstance()                   { return instance; }
    public ConfigManager getConfigManager()               { return configManager; }
    public DatabaseManager getDatabaseManager()           { return databaseManager; }
    public CodeManager getCodeManager()                   { return codeManager; }
    public RoleSyncManager getRoleSyncManager()           { return roleSyncManager; }
    public RewardManager getRewardManager()               { return rewardManager; }
    public CacheManager getCacheManager()                 { return cacheManager; }
    public LinkSecurityManager getSecurityManager()       { return securityManager; }
    public DiscordBot getDiscordBot()                     { return discordBot; }
    public DiscordManager getDiscordManager()             { return discordManager; }
    public NicknameManager getNicknameManager()           { return nicknameManager; }
    public LuckPermsHook getLuckPermsHook()               { return luckPermsHook; }
}
