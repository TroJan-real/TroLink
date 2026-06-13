package com.trolink.plugin.integrations;

import com.trolink.plugin.TroLink;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    private final Economy economy;

    public VaultHook(TroLink plugin) {
        Economy eco = null;
        if (plugin.getServer().getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager()
                    .getRegistration(Economy.class);
            if (rsp != null) eco = rsp.getProvider();
        }
        this.economy = eco;
    }

    public Economy getEconomy() { return economy; }
    public boolean isEnabled()  { return economy != null; }
}
