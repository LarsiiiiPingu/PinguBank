package de.pingubank.commands;

import de.pingubank.util.BankManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GeldBankCommand implements CommandExecutor {
    private final Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl benutzen.");
            return true;
        }

        double kontostand = BankManager.getBankBalance(player.getUniqueId());
        player.sendMessage("§7Dein Bankguthaben: §e" + kontostand + " Coins");
        return true;
    }
}
