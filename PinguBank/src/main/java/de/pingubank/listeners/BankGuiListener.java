package de.pingubank.listeners;

import de.pingubank.PinguBank;
import de.pingubank.util.AnvilGUI;
import de.pingubank.util.BankManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class BankGuiListener implements Listener {

    private final PinguBank plugin;

    public BankGuiListener(PinguBank plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (event.getView().getTitle().equals("§bBankkonto")) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE) {
                // Einzahlung - öffne AnvilGUI
                new AnvilGUI(plugin, player, input -> {
                    try {
                        double betrag = Double.parseDouble(input);
                        if (betrag <= 0) {
                            player.sendMessage("§cBitte gib eine positive Zahl ein!");
                            return;
                        }

                        if (BankManager.isFrozen(player.getUniqueId())) {
                            player.sendMessage("§7[§b§lBank§r§7] §cDein Bankkonto ist eingefroren!");
                            return;
                        }

                        // Hier kannst du auch prüfen, ob Spieler genug Geld auf Hand hat (falls Handgeld verwaltet wird)
                        BankManager.deposit(player, betrag);
                        player.closeInventory();

                    } catch (NumberFormatException e) {
                        player.sendMessage("§cBitte gib eine gültige Zahl ein!");
                    }
                });

            } else if (event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                // Auszahlung - öffne AnvilGUI
                new AnvilGUI(plugin, player, input -> {
                    try {
                        double betrag = Double.parseDouble(input);
                        if (betrag <= 0) {
                            player.sendMessage("§cBitte gib eine positive Zahl ein!");
                            return;
                        }

                        if (BankManager.isFrozen(player.getUniqueId())) {
                            player.sendMessage("§7[§b§lBank§r§7] §cDein Bankkonto ist eingefroren!");
                            return;
                        }

                        boolean success = BankManager.withdraw(player, betrag);
                        if (!success) {
                            player.sendMessage("§cDu hast nicht genug Geld auf dem Bankkonto!");
                        }
                        player.closeInventory();

                    } catch (NumberFormatException e) {
                        player.sendMessage("§cBitte gib eine gültige Zahl ein!");
                    }
                });
            }
        }
    }
                    }
