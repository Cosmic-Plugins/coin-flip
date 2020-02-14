package me.randomhashtags.coinflip;

import me.randomhashtags.coinflip.addon.CoinFlipStats;
import me.randomhashtags.coinflip.universal.UVersionable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

public class CFPlayer implements UVersionable {
    private static final String folder = COIN_FLIP.getDataFolder() + File.separator + "playerData";
    public static final HashMap<UUID, CFPlayer> players = new HashMap<>();

    private boolean isLoaded;

    public UUID uuid;
    public File file = null;
    public YamlConfiguration yml = null;

    private CoinFlipStats coinflipStats;
    private boolean notifications;

    public CFPlayer(UUID uuid) {
        this.uuid = uuid;
        final File f = new File(folder, uuid.toString() + ".yml");
        boolean backup = false;
        if(!players.containsKey(uuid)) {
            if(!f.exists()) {
                try {
                    final File folder = new File(CFPlayer.folder);
                    if(!folder.exists()) {
                        folder.mkdirs();
                    }
                    f.createNewFile();
                    backup = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            file = new File(folder, uuid.toString() + ".yml");
            yml = YamlConfiguration.loadConfiguration(file);
            players.put(uuid, this);
        }
        if(backup) backup();
    }
    public static CFPlayer get(UUID player) {
        return players.getOrDefault(player, new CFPlayer(player)).load();
    }
    public void backup(boolean async) {
        if(async) SCHEDULER.runTaskAsynchronously(COIN_FLIP, () -> backup());
        else backup();
    }
    public void backup() {
        yml.set("name", Bukkit.getOfflinePlayer(uuid).getName());

        if(coinflipStats != null) {
            yml.set("notifications", notifications);
            yml.set("wins", coinflipStats.wins);
            yml.set("wonCash", coinflipStats.wonCash);
            yml.set("losses", coinflipStats.losses);
            yml.set("lostCash", coinflipStats.lostCash);
            yml.set("taxesPaid", coinflipStats.taxesPaid);
        }

        save();
    }
    public CFPlayer load() {
        if(!isLoaded) {
            isLoaded = true;
            notifications = yml.getBoolean("notifications");
            return this;
        }
        return players.get(uuid);
    }
    public void unload() {
        if(isLoaded) {
            isLoaded = false;
            try {
                backup();
            } catch (Exception e) {
                e.printStackTrace();
            }
            players.remove(uuid);
        }
    }
    private void save() {
        try {
            yml.save(file);
            yml = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public UUID getUUID() { return uuid; }
    public OfflinePlayer getOfflinePlayer() {
        return uuid != null ? Bukkit.getOfflinePlayer(uuid) : null;
    }

    public CoinFlipStats getCoinFlipStats() {
        if(coinflipStats == null) {
            final String c = yml.getString("coinflip stats");
            if(c != null && !c.isEmpty()) {
                final String[] s = c.split(";");
                coinflipStats = new CoinFlipStats(getBigDecimal(s[0]), getBigDecimal(s[1]), getBigDecimal(s[2]), getBigDecimal(s[3]), getBigDecimal(s[4]));
            } else {
                final BigDecimal z = BigDecimal.ZERO;
                coinflipStats = new CoinFlipStats(z, z, z ,z, z);
            }
        }
        return coinflipStats;
    }
    public boolean doesReceiveCoinFlipNotifications() {
        return notifications;
    }
    public void setReceivesCoinFlipNotifications(boolean bool) {
        notifications = bool;
    }
}
