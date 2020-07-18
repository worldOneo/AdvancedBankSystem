package de.worldOneo.advancedBankSystem.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface IGUI {
    boolean handle(InventoryClickEvent e);

    String getGUITitle();

    Inventory render();

    Player getPlayer();
}
