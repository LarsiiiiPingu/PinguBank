package de.pinguinobank;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class PinguBank extends JavaPlugin {

    public static PinguBank instance;
    private FileConfiguration config;

    private HashMap<UUID, Double> kontostand = new HashMap<>();
    private HashMap<UUID, Boolean> kreditAktiv = new HashMap<>();
    private HashMap<UUID, Long> kreditZeit = new HashMap<>();
    private HashMap<UUID, Double> kreditBetrag = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        config = getConfig();
        Bukkit.getLogger().info("[PinguBank] Plugin aktiviert.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[PinguBank] Plugin deaktiviert.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (command.getName().equalsIgnoreCase("bank")) {
            double stand = kontostand.getOrDefault(uuid, config.getDouble("startgeld"));
            player.sendMessage("§bDein Kontostand: §e" + stand + " Coins");
            return true;
        }

        if (command.getName().equalsIgnoreCase("kredit")) {
            if (kreditAktiv.getOrDefault(uuid, false)) {
                player.sendMessage("§cDu hast bereits einen aktiven Kredit.");
                return true;
            }

            kreditAktiv.put(uuid, true);
            kreditZeit.put(uuid, System.currentTimeMillis());
            kreditBetrag.put(uuid, config.getDouble("kredit.max"));
            kontostand.put(uuid, kontostand.getOrDefault(uuid, config.getDouble("startgeld")) + config.getDouble("kredit.max"));

            player.sendMessage("§aDu hast einen Kredit über §e" + config.getDouble("kredit.max") + " Coins erhalten.");
            return true;
        }

        return false;
    }

    public static PinguBank getInstance() {
        return instance;
    }

    public double berechneZinsen(UUID uuid) {
        if (!kreditAktiv.containsKey(uuid) || !kreditAktiv.get(uuid)) return 0;

        long start = kreditZeit.get(uuid);
        long tageVergangen = (System.currentTimeMillis() - start) / (1000L * 60 * 60 * 24);

        if (tageVergangen <= 50) return 0;

        double zinsen = kreditBetrag.get(uuid);
        for (int i = 0; i < (tageVergangen - 50); i++) {
            zinsen *= 1.0128;
        }

        return Math.round((zinsen - kreditBetrag.get(uuid)) * 100.0) / 100.0;
    }
}