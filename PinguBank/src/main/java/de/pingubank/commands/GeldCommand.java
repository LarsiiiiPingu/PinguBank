package de.pingubank.commands;

import org.bukkit.command.Command;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GeldCommand implements CommandExecutor {
    private final Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl benutzen.");
            return true;
        }

        double geld = economy.getBalance(player); // Handgeld (normales Vault-Konto)
        player.sendMessage("§7Dein Geld: §e" + geld + " Coins");
        return true;
    }
}
