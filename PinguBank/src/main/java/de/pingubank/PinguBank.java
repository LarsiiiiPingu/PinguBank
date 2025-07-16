package de.pingubank;

import de.pingubank.commands.BankCommand;
import de.pingubank.commands.BankFreezeCommand;
import de.pingubank.commands.BankUnfreezeCommand;
import de.pingubank.commands.GeldCommand;
import de.pingubank.commands.GeldBankCommand;
import de.pingubank.commands.PayCommand;
import de.pingubank.commands.TransferCommand;
import de.pingubank.listeners.BankGuiListener;
import de.pingubank.util.BankManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PinguBank extends JavaPlugin {

    private static PinguBank instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("§b[PinguBank] Plugin aktiviert.");

        // Bank-Daten laden
        BankManager.loadData();

        // Alle Befehle registrieren
        getCommand("bank").setExecutor(new BankCommand(this));
        getCommand("geld").setExecutor(new GeldCommand());
        getCommand("geldbank").setExecutor(new GeldBankCommand());
        getCommand("pay").setExecutor(new PayCommand());
        getCommand("transfer").setExecutor(new TransferCommand());
        getCommand("bankfreeze").setExecutor(new BankFreezeCommand());
        getCommand("bankunfreeze").setExecutor(new BankUnfreezeCommand());

        // GUI-Listener registrieren
        getServer().getPluginManager().registerEvents(new BankGuiListener(this), this);
    }

    @Override
    public void onDisable() {
        BankManager.saveData();
        getLogger().info("§c[PinguBank] Plugin deaktiviert.");
    }

    public static PinguBank getInstance() {
        return instance;
    }
    }
