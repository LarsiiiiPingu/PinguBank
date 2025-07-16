package de.pingubank.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GeldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl benutzen.");
            return true;
        }

        double geld = player.getBalance(); // Handgeld (normales Vault-Konto)
        player.sendMessage("§7Dein Geld: §e" + geld + " Coins");
        return true;
    }
}
