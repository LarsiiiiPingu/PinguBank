package de.pingubank.commands;

import de.pingubank.util.BankManager;
import org.bukkit.Bukkit;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TransferCommand implements CommandExecutor {
    private final Economy economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player senderPlayer)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl verwenden.");
            return true;
        }

        if (args.length != 2) {
            senderPlayer.sendMessage("§cVerwendung: /transfer <Spieler> <Betrag>");
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

        if (betrag <= 0) {
            senderPlayer.sendMessage("§cDer Betrag muss größer als 0 sein.");
            return true;
        }

        double senderKonto = BankManager.getBankBalance(senderPlayer.getUniqueId());
        if (senderKonto < betrag) {
            senderPlayer.sendMessage("§cDu hast nicht genug Geld auf deinem Bankkonto.");
            return true;
        }

        // Übertragen
        BankManager.withdraw(senderPlayer, betrag);
        BankManager.deposit(target, betrag);

        senderPlayer.sendMessage("§7Du Hast Erfolgreich §e" + betrag + " §7An §3" + target.getName() + " §aÜberwiesen§7.");
        target.sendMessage("§7Du Hast §e" + betrag + " §7Von §3" + senderPlayer.getName() + " §aErhalten.");

        Bukkit.getLogger().info("[Bank] " + senderPlayer.getName() + " Transfer Zu " + target.getName() + " Summe: " + betrag);
        return true;
    }
}
