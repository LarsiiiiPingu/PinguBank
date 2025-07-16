package de.pingubank;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PinguBank extends JavaPlugin {

    private static PinguBank instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("§b[PinguBank] Plugin aktiviert.");

        // Command-Registrierung
        getCommand("bank").setExecutor(new commands.BankCommand());
        getCommand("geld").setExecutor(new commands.GeldCommand());
        getCommand("geldbank").setExecutor(new commands.GeldBankCommand());
        getCommand("pay").setExecutor(new commands.PayCommand());
        getCommand("transfer").setExecutor(new commands.TransferCommand());
        getCommand("bankfreeze").setExecutor(new commands.BankFreezeCommand());
        getCommand("bankunfreeze").setExecutor(new commands.BankUnfreezeCommand());

        // Listener registrieren
        getServer().getPluginManager().registerEvents(new listener.InventoryClickListener(), this);
        // TODO: Daten laden, falls nötig
        BankManager.loadData();
    }

    @Override
    public void onDisable() {
        getLogger().info("§c[PinguBank] Plugin deaktiviert.");
        // TODO: Daten speichern, falls nötig
        BankManager.saveData();
    }

    public static PinguBank getInstance() {
        return instance;
    }
}
