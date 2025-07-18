package de.pingubank.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AnvilGUI implements Listener {

    private final Plugin plugin;
    private static final Map<Player, AnvilGUI> openedInventories = new HashMap<>();

    private final Player player;
    private final Consumer<String> onComplete;

    public AnvilGUI(Plugin plugin, Player player, Consumer<String> onComplete) {
        this.plugin = plugin;
        this.player = player;
        this.onComplete = onComplete;

        open();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        openedInventories.put(player, this);
    }

    private void open() {
        Inventory inv = Bukkit.createInventory(null, 9, "§7Gib einen Betrag ein");

        // Item zum Beschriften im Slot 0 (Anvil links)
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName("§aGib hier deinen Betrag ein");
        paper.setItemMeta(meta);
        inv.setItem(0, paper);

        // Leeres Item in Slot 1 und 2 (Anvil slots)
        inv.setItem(1, new ItemStack(Material.AIR));
        inv.setItem(2, new ItemStack(Material.AIR));

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player clickedPlayer)) return;
        if (!openedInventories.containsKey(clickedPlayer)) return;

        if (event.getInventory().getType() != org.bukkit.event.inventory.InventoryType.ANVIL
            && !event.getView().getTitle().equals("§7Gib einen Betrag ein")) return;

        event.setCancelled(true);

        if (event.getRawSlot() == 2) { // Ausgabe Slot (Rechts)
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) return;

            String input = clicked.getItemMeta().getDisplayName();
            close(clickedPlayer);
            onComplete.accept(input);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player p = (Player) event.getPlayer();
        if (!openedInventories.containsKey(p)) return;

        openedInventories.remove(p);
        HandlerList.unregisterAll(this);
    }

    private void close(Player player) {
        player.closeInventory();
        openedInventories.remove(player);
        HandlerList.unregisterAll(this);
    }
          }
      
