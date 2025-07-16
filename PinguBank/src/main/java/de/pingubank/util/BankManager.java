package de.pingubank.util;

import de.helmion.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BankManager {

    private static final HashMap<UUID, Double> bankKonten = new HashMap<>();
    private static final HashMap<UUID, Boolean> freezeStatus = new HashMap<>();

    public static double getBankBalance(UUID uuid) {
        return bankKonten.getOrDefault(uuid, 0.0);
    }

    public static boolean isFrozen(UUID uuid) {
        return freezeStatus.getOrDefault(uuid, false);
    }

    public static void freeze(UUID uuid) {
        freezeStatus.put(uuid, true);
    }

    public static void unfreeze(UUID uuid) {
        freezeStatus.put(uuid, false);
    }

    public static AnvilGUI.Response deposit(Player player, double betrag) {
        UUID uuid = player.getUniqueId();
        if (isFrozen(uuid)) {
            player.sendMessage("§7[§b§lBank§r§7] §7Dein Bankkonto Ist Eingefroren.");
            return AnvilGUI.Response.close();
        }

        double neuerBetrag = getBankBalance(uuid) + betrag;
        bankKonten.put(uuid, neuerBetrag);
        player.sendMessage("§7[§b§lBank§r§7] §aEinzahlung erfolgreich§7. Neuer Kontostand: §e" + neuerBetrag);
        return AnvilGUI.Response.close();
    }

    public static AnvilGUI.Response withdraw(Player player, double betrag) {
        UUID uuid = player.getUniqueId();
        if (isFrozen(uuid)) {
            player.sendMessage("§7[§b§lBank§r§7] §7Dein Bankkonto Ist Eingefroren.");
            return AnvilGUI.Response.close();
        }

        double aktuell = getBankBalance(uuid);
        if (betrag > aktuell) {
            return AnvilGUI.Response.text("§cZu wenig Geld!");
        }

        bankKonten.put(uuid, aktuell - betrag);
        player.sendMessage("§7[§b§lBank§r§7] §aAuszahlung erfolgreich§7. Neuer Kontostand: §e" + (aktuell - betrag));
        return AnvilGUI.Response.close();
    }
}
