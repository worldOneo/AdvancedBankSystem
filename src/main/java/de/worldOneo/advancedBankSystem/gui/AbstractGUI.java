package de.worldOneo.advancedBankSystem.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public abstract class AbstractGUI implements IGUI {

    @Override
    public boolean handle(InventoryClickEvent e) {
        e.setCancelled(true);
        return false;
    }

    @Override
    public void open(Player player, Consumer<Object> callback) {

    }

    @Override
    public void open(Player player, Object obj, Consumer<Object> callback) {

    }
}
