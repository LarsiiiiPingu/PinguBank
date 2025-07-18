package de.pingubank.commands;

import org.bukkit.Bukkit;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player senderPlayer)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl verwenden.");
            return true;
        }

        if (args.length != 2) {
            senderPlayer.sendMessage("§cVerwendung: /pay <Spieler> <Betrag>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || target == senderPlayer) {
            senderPlayer.sendMessage("§cUngültiger Spieler.");
            return true;
        }

        double betrag;
        try {
            betrag = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            senderPlayer.sendMessage("§cUngültiger Betrag.");
            return true;
        }

        if (betrag <= 0 || betrag > 5000) {
            senderPlayer.sendMessage("§cBetrag muss zwischen 1 und 5000 Coins liegen.");
            return true;
        }

        if (senderPlayer.getBalance() < betrag) {
            senderPlayer.sendMessage("§cDu hast nicht genug Geld.");
            return true;
        }

        senderPlayer.withdrawPlayer(betrag);
        target.depositPlayer(betrag);

        senderPlayer.sendMessage("§7Du Hast §e" + betrag + " §7An §3" + target.getName() + " §aGezahlt§7.");
        target.sendMessage("§7Du Hast §e" + betrag + " §7Von §3" + senderPlayer.getName() + " §aErhalten.");

        Bukkit.getLogger().info("[Bank] " + senderPlayer.getName() + " Zahlt Zu " + target.getName() + " Summe: " + betrag);
        return true;
    }
    private final Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
}
