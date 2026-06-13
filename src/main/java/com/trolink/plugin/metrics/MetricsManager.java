package com.trolink.plugin.metrics;

import com.trolink.plugin.TroLink;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;

/**
 * bStats entegrasyon yöneticisi.
 *
 * bStats entegrasyon yöneticisi.
 * Plugin sayfası: https://bstats.org/plugin/bukkit/TroLink/31966
 */
public class MetricsManager {

    private static final int BSTATS_PLUGIN_ID = 31966;

    private final TroLink plugin;
    private Metrics metrics;

    public MetricsManager(TroLink plugin) {
        this.plugin = plugin;
        init();
    }

    private void init() {
        this.metrics = new Metrics(plugin, BSTATS_PLUGIN_ID);

        addCustomCharts();

        plugin.getLogger().info("[bStats] İstatistik takibi aktif. " +
                "https://bstats.org/plugin/bukkit/TroLink/" + BSTATS_PLUGIN_ID);
    }

    private void addCustomCharts() {
        // Veritabanı türü (SQLite vs MySQL gibi gelecekteki seçenekler için)
        metrics.addCustomChart(new SimplePie("database_type", () -> "SQLite"));

        // LuckPerms entegrasyonu aktif mi?
        metrics.addCustomChart(new SimplePie("luckperms_enabled", () ->
                plugin.getServer().getPluginManager().isPluginEnabled("LuckPerms") ? "Aktif" : "Pasif"));

        // PlaceholderAPI entegrasyonu aktif mi?
        metrics.addCustomChart(new SimplePie("placeholderapi_enabled", () ->
                plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") ? "Aktif" : "Pasif"));

        // Vault entegrasyonu aktif mi?
        metrics.addCustomChart(new SimplePie("vault_enabled", () ->
                plugin.getServer().getPluginManager().isPluginEnabled("Vault") ? "Aktif" : "Pasif"));

        // Toplam bağlı hesap sayısı (anlık)
        metrics.addCustomChart(new SingleLineChart("linked_accounts", () -> {
            try {
                return plugin.getCacheManager().getCachedCount();
            } catch (Exception e) {
                return 0;
            }
        }));
    }

    public Metrics getMetrics() {
        return metrics;
    }
}
