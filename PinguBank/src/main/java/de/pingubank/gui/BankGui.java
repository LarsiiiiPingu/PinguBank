package de.pingubank.gui;

import de.pingubank.util.BankManager;
import de.helmion.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.util.UUID;

public class BankGUI {

    public static void openBankGUI(Player player) {
        if (BankManager.isFrozen(player.getUniqueId())) {
            player.sendMessage("§7[§b§lBank§r§7] §7Dein Bankkonto Ist Eingefroren. §cAktuell Hast Du Keine Möglichkeit Dein Konto Zu Verwalten.");
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 27, "§b§lDeine Bank");

        // Kopf in der Mitte (Bankkontostand)
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta skullMeta = skull.getItemMeta();
        skullMeta.setDisplayName("§3Bankkontostand:");
        double balance = BankManager.getBankBalance(player.getUniqueId());
        skullMeta.setLore(java.util.List.of("§7" + balance + " Coins"));
        skull.setItemMeta(skullMeta);
        inv.setItem(4, skull);

        // Einzahlung (grüne Keramik links)
        ItemStack einzahlen = new ItemStack(Material.GREEN_GLAZED_TERRACOTTA);
        ItemMeta einzahlenMeta = einzahlen.getItemMeta();
        einzahlenMeta.setDisplayName("§aEinzahlen");
        einzahlen.setItemMeta(einzahlenMeta);
        inv.setItem(11, einzahlen);

        // Auszahlung (rote Keramik rechts)
        ItemStack auszahlen = new ItemStack(Material.RED_GLAZED_TERRACOTTA);
        ItemMeta auszahlenMeta = auszahlen.getItemMeta();
        auszahlenMeta.setDisplayName("§cAuszahlen");
        auszahlen.setItemMeta(auszahlenMeta);
        inv.setItem(15, auszahlen);

        player.openInventory(inv);
    }

    public static void handleClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("§b§lDeine Bank")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;

            String name = clicked.getItemMeta().getDisplayName();

            if (name.equals("§aEinzahlen")) {
                player.closeInventory();
                new AnvilGUI.Builder()
                        .plugin(Bukkit.getPluginManager().getPlugin("PinguBank"))
                        .title("Betrag einzahlen")
                        .text("")
                        .onComplete((p, text) -> {
                            try {
                                double betrag = Double.parseDouble(text);
                                return BankManager.deposit(p, betrag);
                            } catch (NumberFormatException e) {
                                return AnvilGUI.Response.text("§cNur Zahlen!");
                            }
                        })
                        .open(player);
            }

            if (name.equals("§cAuszahlen")) {
                player.closeInventory();
                new AnvilGUI.Builder()
                        .plugin(Bukkit.getPluginManager().getPlugin("PinguBank"))
                        .title("Betrag auszahlen")
                        .text("")
                        .onComplete((p, text) -> {
                            try {
                                double betrag = Double.parseDouble(text);
                                return BankManager.withdraw(p, betrag);
                            } catch (NumberFormatException e) {
                                return AnvilGUI.Response.text("§cNur Zahlen!");
                            }
                        })
                        .open(player);
            }
        }
    }
                                    }
