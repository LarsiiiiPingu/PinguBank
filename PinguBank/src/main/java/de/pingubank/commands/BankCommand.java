package de.pingubank.commands;

import de.pingubank.PinguBank;
import de.pingubank.util.AnvilGUI;
import de.pingubank.util.BankManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class BankCommand implements CommandExecutor {

    private final PinguBank plugin;

    public BankCommand(PinguBank plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können das benutzen.");
            return true;
        }

        // Bankkonto eingefroren?
        UUID uuid = player.getUniqueId();
        if (BankManager.isFrozen(uuid)) {
            player.sendMessage("§7[§b§lBank§r§7] §7Dein Bankkonto Ist Eingefroren. §cAktuell keine Verwaltung möglich.");
            return true;
        }

        Inventory inv = Bukkit.createInventory(null, 27, "§bBankkonto");

        // 1. Reihe Mitte: Kopf mit Bankkontostand
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setOwningPlayer(player);
        headMeta.setDisplayName("§eDein Bankkonto: §a" + BankManager.getBankBalance(uuid));
        head.setItemMeta(headMeta);

        inv.setItem(13, head);

        // 2. Reihe Links: Grüner Keramikblock = Einzahlen
        ItemStack green = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta greenMeta = green.getItemMeta();
        greenMeta.setDisplayName("§aEinzahlen");
        green.setItemMeta(greenMeta);
        inv.setItem(9, green);

        // 2. Reihe Rechts: Roter Keramikblock = Auszahlen
        ItemStack red = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta redMeta = red.getItemMeta();
        redMeta.setDisplayName("§cAuszahlen");
        red.setItemMeta(redMeta);
        inv.setItem(17, red);

        player.openInventory(inv);

        // Listener zum Handling der Klicks:  
        // Wir brauchen hier einen Listener (oder den bestehenden), der das anklicken von grüner/roter Keramik behandelt  
        // und dann die AnvilGUI öffnet.

        // Da das per CommandExecutor nicht geht, solltest du einen Listener registrieren, der diesen GUI-Klick erkennt.

        return true;
    }
                         }
