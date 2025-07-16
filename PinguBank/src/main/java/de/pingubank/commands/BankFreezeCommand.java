package de.pingubank.commands;

import de.pingubank.util.BankManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BankFreezeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("pingubank.admin")) {
            sender.sendMessage("§cKeine Rechte.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§cVerwendung: /bankfreeze <Spieler>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        BankManager.freeze(target.getUniqueId());

        String msg = "§7[§b§lBank§r§7] §3" + target.getName() + " §7Bank Wurde §1Eingefroren§7.";
        sender.sendMessage(msg);
        Bukkit.getLogger().info("[Bank] Konto von " + target.getName() + " wurde eingefroren.");

        if (target.isOnline()) {
            target.getPlayer().sendMessage("§7[§b§lBank§r§7] §7Dein Bankkonto Ist Eingefroren. §cAktuell Hast Du Keine Möglichkeit Dein Konto Zu Verwalten.");
        }

        return true;
    }
}
