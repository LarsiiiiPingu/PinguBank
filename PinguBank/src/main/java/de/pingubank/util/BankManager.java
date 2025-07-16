package de.pingubank.util;

import de.pingubank.PinguBank;
import de.helmion.anvilgui.AnvilGUI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class BankManager {

    private static final HashMap<UUID, Double> bankKonten = new HashMap<>();
    private static final HashMap<UUID, Boolean> freezeStatus = new HashMap<>();

    private static File dataFile;
    private static FileConfiguration dataConfig;

    // Aufruf beim Pluginstart
    public static void loadData() {
        dataFile = new File(PinguBank.getInstance().getDataFolder(), "bankdata.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            PinguBank.getInstance().saveResource("bankdata.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        // Lade Bankkonten
        for (String key : dataConfig.getConfigurationSection("bankKonten").getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            double balance = dataConfig.getDouble("bankKonten." + key);
            bankKonten.put(uuid, balance);
        }

        // Lade Freeze Status
        for (String key : dataConfig.getConfigurationSection("freezeStatus").getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            boolean frozen = dataConfig.getBoolean("freezeStatus." + key);
            freezeStatus.put(uuid, frozen);
        }
    }

    // Aufruf beim Pluginstop
    public static void saveData() {
        for (UUID uuid : bankKonten.keySet()) {
            dataConfig.set("bankKonten." + uuid.toString(), bankKonten.get(uuid));
        }
        for (UUID uuid : freezeStatus.keySet()) {
            dataConfig.set("freezeStatus." + uuid.toString(), freezeStatus.get(uuid));
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double getBankBalance(UUID uuid) {
        return bankKonten.getOrDefault(uuid, 0.0);
    }

    public static boolean isFrozen(UUID uuid) {
        return freezeStatus.getOrDefault(uuid, false);
    }

    public static void freeze(UUID uuid) {
        freezeStatus.put(uuid, true);
        saveData();
    }

    public static void unfreeze(UUID uuid) {
        freezeStatus.put(uuid, false);
        saveData();
    }

    public static boolean deposit(Player player, double betrag) {
    UUID uuid = player.getUniqueId();
    if (isFrozen(uuid)) {
        player.sendMessage("§7[§b§lBank§r§7] §7Dein Bankkonto Ist Eingefroren.");
        return false;
    }

    // Hier könntest du noch prüfen, ob Spieler genug Handgeld hat (falls implementiert)

    double neuerBetrag = getBankBalance(uuid) + betrag;
    bankKonten.put(uuid, neuerBetrag);
    player.sendMessage("§7[§b§lBank§r§7] §aEinzahlung erfolgreich§7. Neuer Kontostand: §e" + neuerBetrag);
    saveData();
    return true;
}

public static boolean withdraw(Player player, double betrag) {
    UUID uuid = player.getUniqueId();
    if (isFrozen(uuid)) {
        player.sendMessage("§7[§b§lBank§r§7] §7Dein Bankkonto Ist Eingefroren.");
        return false;
    }

    double aktuell = getBankBalance(uuid);
    if (betrag > aktuell) {
        return false;
    }

    bankKonten.put(uuid, aktuell - betrag);
    player.sendMessage("§7[§b§lBank§r§7] §aAuszahlung erfolgreich§7. Neuer Kontostand: §e" + (aktuell - betrag));
    saveData();
    return true;
}
    
}
