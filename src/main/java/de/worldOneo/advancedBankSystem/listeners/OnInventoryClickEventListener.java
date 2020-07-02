package de.worldOneo.advancedBankSystem.listeners;

import de.worldOneo.advancedBankSystem.manager.GUIManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OnInventoryClickEventListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        GUIManager.getInstance().handle(e);
    }
}
