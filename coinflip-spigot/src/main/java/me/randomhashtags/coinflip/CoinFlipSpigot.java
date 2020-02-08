package me.randomhashtags.coinflip;

import org.bukkit.plugin.java.JavaPlugin;

public final class CoinFlipSpigot extends JavaPlugin {

    public static CoinFlipSpigot getPlugin;

    @Override
    public void onEnable() {
        getPlugin = this;
        saveSettings();
        enable();
    }

    @Override
    public void onDisable() {
        disable();
    }

    private void saveSettings() {
        saveDefaultConfig();
    }

    public void enable() {
        saveSettings();
        CoinFlipAPI.INSTANCE.load();
    }
    public void disable() {
        CoinFlipAPI.INSTANCE.unload();
    }

    public void reload() {
        disable();
        enable();
    }
}
